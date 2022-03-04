
from netmiko import Netmiko
import traceback, threading, time
import sys, os, logging, fire, csv
from os import path
from random import randint
from string import Template

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
