# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

[Phase 2 Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBF+wAIIgQKLl0wATeQCNgXFDA3bMmdlAgBXbDADEaYFQCerDt178kg2wHcAFkjAxRFRSAFoAPnJKGigALhgAbQAFAHkyABUAXRgAegt9KAAdNABvfMp7AFsUABoYXDVvaA06lErgJAQAX0xhGJgIl04ePgEhaNF4qFceSgAKcqgq2vq9LiaoFpg2joQASkw2YfcxvtEByLkwRWVVLnj2FDAAVQKFguWDq5uVNQvDbTxMgAUQAMsC4OkYItljAAGbmSowV6Ub6Yb5KX5cAaDI5uUaecYiFTxNAWBAIQ4zE6Es4qf4Y25qeIgab8FAoqDvCrAap1BrrZpfeSYu7-IzxACSADkQSwoTDeSsBRstjtOjAZekUsiCmi8SMPII6QZBoysSy2YEFBYwL5uUslcLrqK-oMJZrZcD5dCPkq6sBbb50hAANboT3amA2u1o81i0K46kE40TEkwQN2kPhtBU44polUc6JyJ9SjxTPBsPoXrRSg4yLBdBgeIAJgADO3iiVK9n0D10BoTGZLNYbNBpI8YKCIJw0D5-IFME2wqW63FEqkMtkcvo1ITu4q+atGs0emXYEn80bC5MYAhZ0g0A7lvy1qqDgaaaniabLiKmXuGBHheN4jxQZ0fgTSIJRBcFIV9HlqnhRFdVReR0QArEGyGfEbxNeJFjzPDTjTP8vwLAiYBiJA4UcJJ32aOYVSFYjDVI38GSwu5LRQdkY3tcDINdbF3UBT05ShXtq3nLUdQEuNuLdK8SNpMiKyDPtcwo-CyJwi8NKzGTa2oesS3gZBmzbTtu2knMBzQIdTHMKxbDMFAIxndhLGYGw-ACIJLNXKJTI3BIZDBYF0mBHc9y4A97E04yL3+HSOKLdMH28205js9BP2TXTOLNJSgI0FAECeFABK03KkpzYTAPFQEIvBaKM3qiMEQgJEFIw+NlMiNK1N-az2zY79b3pcyDJgDtxpS8yVxbObO0wQdhxcsdpg0ac3BgABxJVsT8xdApCZhBlmhIDqind2CVYo8tzRahsK9K72QHgjuqLg6qMhqJsovSSpdQD4houjOX+qtAYG0SYMBOCISkzr5263qg31d6Royv94YeJ4FApH7VDmRrsLE2JkYQvA+GcCA4WApVvhgOYHuqSUZDqfzAkQx1j20BBQFDYpwLqDmUGlJ0YCfQ6Wf60qcOGn88YrKB6ZgRnmeqbHrw+6arvXdXNe1yW0UWwZlrGp6NeABmmfNjCNuc0dbGwCwoGwSr4CtAxSYXALlyCy611C+JkjSLJcklp60e7SXpeqc911SnHVbvVk+MCUmYa0iWlSTiCgaKvGLgJ6jKFoxxoeeinoIBIFItptGUJ66MsYwlWpr-GAK5A3PE6dTCwcpxHqebqEh+QjH5d1xXR4TCJu6oskKRLg38dK3j2UHwvh-h5rYhlSSdal-0YDQFBvFJrnIx1UnFMXwbcPY3G70lrmN-fw2w5iB4lRf0to2EONs0CfxkOtRym03Y2EcBVB83gYAACkIBywDjYIWItg4XX+NdJIzxo45FjolAG6BuzLTgBAB8UAC6cxkCnUKad9Y-xQPEAAVmgtAudnp1EodQ6AdCUBcwKiwjO01wgV0hjXN4dcR5QTUBEWCk8OpkPRqhPq1xv7iN7v3J4e9qhF3rm6ceNMp77xnqhR+Xd0493LtvDMGgNDQ34TQuoQl5EiSPifb0Co-TITknPFArN+AWQulQmhnimrmRXupVa41YnFSNuHeJJl+hLVAfE4oECoFORHK5GwZh7YWT4rAYA2BvaEBvIHJcy08HG0SK1KKMVchGDSWZN6Yie7xHKpVQI3xybaLsRECuvSqqcj+sYrgSiWqRXasACk-NWZy05HrVSOiuLPzKhVKqpNJlRLHo3Jp8zFlO2uLLec1itGJLLiMhxYzrSd2uIMw+VNjnAgzIsysyz5yaOAAcpenT1ndNSTc4syT-6pOAWEqyqSNpAA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
