
from netmiko import Netmiko
import traceback, threading, time
import sys, os, logging, fire, csv
from os import path
from random import randint
from string import Template
from ntc_templates.parse import parse_output
import json
import ast

logging.basicConfig(filename='dp.log', level=logging.INFO, format='%(asctime)s_%(threadName)s %(message)s', )


def execute(device, commands, outputDirectory) :
    executionStatus = False
    reasonForFailure = "NA"
    executionProtocol = "SSH"

    try :
        logging.info('verifying the existence of the output folder : %s', outputDirectory)
        if not os.path.exists(outputDirectory) :
            logging.info('outputDirectory : %s not available, so creating one...')
            os.mkdir(outputDirectory)

        logging.info('started...')
        net_connect = Netmiko(**device)
        hostname = str(net_connect.find_prompt())[:-1]
        logging.info('prompt identified as : %s', net_connect.find_prompt())
        output = 'Device IP : ' + device['host'] + '\n'

        for commandline in commands :
            command = commandline.rstrip('\r\n')
            commandOutput = net_connect.send_command(command)
            #logging.info('command : %s : executed with output : %s ', command, commandOutput)
            output += 'Command : ' + command + '\n'
            output += commandOutput
            deviceInterfaces = parse_output(platform=device['device_type'], command=command, data=commandOutput)
            outFileName = outputDirectory + '/' + device['host'] + '.log' if outputDirectory[:1] == '/' else outputDirectory + device['host'] + '.log'
            logging.info('outFilename : %s', outFileName)
            # logging.info('command : %s : executed with json output : %s : with type : %s', command, commandJsonOutput, type(commandJsonOutput))
            # non dp code. need to be removed later
            # deviceInterfaces = ast.literal_eval(commandJsonOutput)

            for deviceInterface in deviceInterfaces:
                # logging.info('interface name : %s, status : %s, protocol : %s, descr : %s', type(deviceInterface['port']), deviceInterface['status'], deviceInterface['protocol'], deviceInterface['descrip'])
                interfaceName = str(deviceInterface['port'])
                if interfaceName.startswith('Po'):
                    logging.info('executiong for interface : %s', interfaceName)
                    interfaceCommand = 'show run int ' + interfaceName
                    interfaceOutput = 'Device IP : ' + device['host'] + '\n'
                    interfaceOutput = 'HostName : ' + hostname + '\n'
                    interfaceOutput += 'Interface Name : ' + interfaceName + '\n'
                    interfaceOutput += 'Command : ' + interfaceCommand + '\n\n'
                    interfaceCommandOutput = net_connect.send_command(interfaceCommand)
                    interfaceOutput += interfaceCommandOutput
                    interfaceFilename = outputDirectory + '/' + device['host'] + '_' + interfaceName + '.log'
                    interfaceOutputFile = open(interfaceFilename, 'w')
                    interfaceOutputFile.write(interfaceOutput); interfaceOutputFile.flush(); interfaceOutputFile.close()
                    logging.info('interface file written for : %s', interfaceFilename)

                    # interfaceServiceInstances = parse_output(platform=device['device_type'], command=interfaceCommand, data=interfaceCommandOutput)
                    # for interfaceServiceInstance in interfaceServiceInstances:
                        # print('service instance : %s', interfaceServiceInstance)

        # logging.info('writing output : %s', output)
        logging.info('completed executing command, redirecting to output file %s', outFileName)
        outFile = open(outFileName, 'w')
        outFile.write(output); outFile.flush(); outFile.close()
        logging.info('completed... disconnecting...')
        net_connect.disconnect()
    except :
        logging.error('exception in thread function for %s ip , stack : %s', threading.currentThread().getName(), traceback.extract_stack())
        raise

def push(inputIpsFile, commandsFile, outputDirectory, username, password) :

    threading.currentThread().setName('pushCli')

    logging.info('input ips : %s', inputIpsFile)
    logging.info('output directory : %s ' , outputDirectory)

    if not path.exists(sys.argv[5]) :
        os.mkdir(sys.argv[5])

    inputIps = open(inputIpsFile, 'r').readlines()
    logging.info('input ips : %s', inputIps)

    commands = open(commandsFile, 'r').readlines()

    dev = {
        'username': username,
        'password': password,
        'device_type': 'cisco_ios'
    }

    try :
        for ip in inputIps :
            device = dict(dev)
            device['host'] = ip.rstrip('\r\n')
            thread = threading.Thread(name='Thread-' + device['host'], target=execute, args=(device, commands, outputDirectory))
            thread.setDaemon(True);
            logging.info('going to start thread for : %s', device['host'])
            thread.start()
    except :
        traceback.print_exc(file=sys.stdout)

    mainThread = threading.currentThread()
    try :
        for t in threading.enumerate() :
            if t is not mainThread :
                t.join()
    except :
        logging.error(traceback.extract_stack())
    logging.info('finished...')

def tpush( templateFile, csvInput, outputDirectory, username, password) :
    threading.currentThread().setName('pushTemplate')

    if not path.exists(outputDirectory) :
        os.mkdir(outputDirectory)

    device = {
        'username': username,
        'password': password,
        'device_type': 'cisco_ios'
    }

    template = Template(open(templateFile).read().replace('\r\n', ''))
    templateMerge = {}
    with open(csvInput) as csvFile :
        reader = csv.DictReader(csvFile)
        for line in reader :
            commandData = template.substitute(line)
            commandData = commandData.split('\n')
            logging.info('adding commands for %s : %s' , line['ip'], commandData)
            templateMerge[line['ip']] = commandData
        print('total size : ', len(templateMerge))
        print('all values : ' , templateMerge)
    try :
        for ip in templateMerge :
            device['host'] = ip.rstrip('\r\n')
            thread = threading.Thread(name='Thread-' + device['host'], target=execute, args=(device, templateMerge[ip], outputDirectory))
            thread.setDaemon(True); thread.start()
    except :
        traceback.print_exc(file=sys.stdout)

    mainThread = threading.currentThread()
    try :
        for t in threading.enumerate() :
            if t is not mainThread :
                t.join()
    except :
        logging.error(traceback.extract_stack())

if __name__ == '__main__':
    fire.Fire({
        'pushCli'  : push,
        'pushTemplate' : tpush
    })