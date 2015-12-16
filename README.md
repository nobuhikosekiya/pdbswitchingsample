# pdbswitchingsample
A sample application to deploy on Oracle Java Cloud Service and accessing the Oracle Database Cloud Service.

Please note that in order to use multiple PDBs in Oracle Database Cloud Service, you need to use the High Peformance Service or higher.

## Building and deploying the callback jar file
1. See the wiki and create a hudson job to build the callback jar project.  
2. Download the jar file from the job result page.
3. Copy the jar file to ALL of the WebLogic managed server's $DOMAIN_HOME/lib directory. Restart of the server is needed to take affect.
4. See the wiki for and configure the JDBC datasource to use the callback.
 
## Building and deploying the web project
1. See the wiki and create a hudson job to build the web project.
2. Create a Developer Cloud Deploy configuration to deploy the war file and then execute the deploy.

## Using the application
Access the application with : https://your jcs public address/pdb1  
The context root , in this case "pdb1" will change if you specify a different value at the war file build.

The PDB database used by this application will be the one specified at the war file build. So obviously you need the provision a PDB with the same name before accessing by the application.

## Running the sample Selenium test case 
1. See the wiki and create a hudson job that runs the Selenium test.
2. After job completion, in the job result page, you can see browser screenshots and test reports.
