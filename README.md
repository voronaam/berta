# berta
Future Change Impact Analysis Tool

The idea is to be the reverse AppDynamics. While AppDynamics is great in answering the question "what should I fix when X went wrong?", Berta is aimed at answering the question "What should I test if I refactor X?"

It will gather mostly the same information as AppDynamics, but will store it differently. The developer will then ask the tool's web frontend which test's and/or user transactions will be affected.

The tool is aimed to help complex and loosely coupled systems, for which IDE fail to track the dependencies. For example, if the system has a scriptable level in, say, JavaScript that integrates parts of the system referencing methods by name. Even GWT application could derail IDE's refactoring capabilities. In this case it may be easier to gather relevant information at runtime.

Sample usage:

```bash
java -javaagent:path/to/berta.jar=com.example.company.* <other arguments> MainClass
```

