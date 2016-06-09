# berta
Future Change Impact Analysis Tool

The idea is to be the reverse AppDynamics. While AppDynamics is great in answering the question "what should I fix when X went wrong?", Berta is aimed at answering the question "What should I test if I refactor X?"

It will gather mostly the same information as AppDynamics, but will store it differently. The developer will then ask the tool's web frontend which test's and/or user transactions will be affected.

The tool is aimed to help complex and loosely coupled systems, for which IDE fail to track the dependencies. For example, if the system has a scriptable level in, say, JavaScript that integrates parts of the system referencing methods by name. Even GWT application could derail IDE's refactoring capabilities. In this case it may be easier to gather relevant information at runtime.

Sample usage:

```bash
java -javaagent:path/to/berta.jar=port=8801:handle=seen:transform=com.example.company.* <other arguments> MainClass
```

All parameters optional.

port: port to listen to for the text based API (default 8800)

handle: select the handler for the analyzed methods.

- print: simply prints out a line to stdout every time it encounters an instrumented method

- seen: keeps track of methods seen in the current test (default)

transform: regular expression to match against the full class name in order to instrument it. If omitted, all classes are instrumented. Normal java regular expressions are supported, including
`com.company.package.*|com.company.other.package.*|.*Test` though you may need to escape some symbols for bash (or another shell) to pass a complex regexp.

## Text based API

Berta listens to a port specified (8800 by default) and uses a very simple text (UTF-8) based API to report its results. Upon connection it sends a textual greeting `Welcome to Berta!`

Commands:

- BMAGIC

  A Command to test that Berta is alive. If it is, it responds with a `Magic is here!` line.

- BARGUMENTS

  Diagnostic command that returns the command line agent arguments string that Berta was started with

- Any other string

  Interpreted as a name of the test that is started. Returns all the data gathered for the previous test. The gathered data starts with the name of the test Berta was running (the very first one is always called `Java Startup`), followed by the list of all the methods and their metrics gathered. One method per line. The output ends with an end marker `--END--`.

Sample session (user input is indicated with `>` sign):

```
$ nc localhost 8801
Welcome to Berta!
> BMAGIC
Magic is here!
> BARGUMENTS
port=8801:transform=Te.*|Ab.*:handle=seen
> First Test
Java Startup
Abc#doSomething
Abc#getThisClass
Test#main
--END--
> Second Test
First Test
Abc#doSomething
--END--

```
