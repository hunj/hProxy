# hProxy
## a simple Java proxy that will run on your own computer/server

Hun Jae Lee (hxl224) EECS325 Project1 due 160322

## Instructions

### Requirements
- Java version 8 or higher
- UNIX based OS (not tested on Windows), Mac OS X preferred

### Running
- To run, simply do the following: 

```
$ javac *.java; java proxyd -p ###
```

where ### is the number of port you would like to specify. By default, port number 5016 is used.

The proxy will constantly print what it is processing at the moment to the console.  If the console output is too verbose, you can mute it using the `-s` option, like either of the following:

```
$ javac *.java; java proxyd -p ### -s
$ javac *.java; java proxyd -s -p ###
```

It will only print out the necessary GET messages only.


# Notes

- The proxy tries connection on port number 80 initially.

# Issues

- If you use the Dropbox OS-native app, it will constantly check with dropbox.com via port 443 (https) to sync your files but fail, because this proxy can only do HTTP (port 80). 
You need to manually pause Dropbox, or the proxy will keep printing out error messages to the console.

Sites tested:

- cnn.com (takes a while to load)
- case.edu (takes a while to load)
- naver.com