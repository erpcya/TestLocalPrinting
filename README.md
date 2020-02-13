# Test Local Printing
Test Local Printing sending a file for print to [Local Printing](https://github.com/erpcya/LocalPrinting)

Use this source code for send a document directly to printer from external server using [ActiveMQ](http://activemq.apache.org/) as Message Queue
![untitled document](https://user-images.githubusercontent.com/2333092/48182271-f681b180-e300-11e8-9a47-240449765b76.png)

Just run it like is showed:

<pre>
./test-print <FileName> <AD_AppRegistration_ID>
</pre>

- FileName: Path of file to send
- AD_AppRegistration_ID: ID of App registration for get config

Note: Server / Producer is not here!!! You can implement like is showed from official ActiveMQ for java: http://activemq.apache.org/hello-world.html