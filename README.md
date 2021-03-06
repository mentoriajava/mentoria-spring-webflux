# Spring Mentorship

This project is used as base reference for someone who wants to learn something new or wants to enter the spring world.

It is intent to be a slow paced, small pieces to be digested by your brain at your own pace. 
This [**Readme**](README.md) file will be your guide to delve into the code, and will point and reference different branches in order to move to a next, more advanced stuff, but remember, this is just to guide you, and help you understand some concepts, this is not the absolute truth.

You can check the latest changes in the [**Changelog**](CHANGELOG.md) file in case you missed some stuff or is just checking out if some reported bug was fixed.

Now let's delve into this repository and let's play with a bunch of concepts and plug it into a lot of other fun stuffs.

## Base idea and base content

The idea is to have some sort of data placeholders to be used as base, with enough complexity to serve a purpose and exemplify something. We used as a base data format some random data obtained from the internet with lots of information related to company.
This includes, it's market value, IPO, acquisitions, and all sort of crazy stuff. This will give us enough complexity for now.

## Git message format

To make git commits easier to automate, we will use a format that makes automation easier. In order to do that, we will make use of the [Conventional Commit](https://www.conventionalcommits.org/en/v1.0.0/) and follow its guidance.

The expected message format is :

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

The possible values for types are:

- **chore** - Some common, mundane task
- **ci** - CI Tasks or things related to CI
- **docs** - Documentation update
- **feat** - Feature introduction (usually updates the MINOR on SemVer)
- **fix** - Fix of something (usually updates the PATCH on SemVer)
- **perf** - Performance related tasks
- **refactor** - Code Refactor
- **revert** - Code reversion
- **style** - Code style changes
- **test** - Test introduction or coverage increment
- **build** - Build related stuff
- **release** - Used to mark a release

## Follow along

To follow this repo and its documentation, check out [our documentation](https://mentoriajava.github.io/mentoria-spring-webflux/).