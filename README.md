# Ollama chat cli

## What is this

This is a tool to prompt your local ollama models, even though there are some tools like this, for example, open web ui,
which is a great ui for the task with chat history and chat context for the model, but there were some reasons to drive
me to make one myself.

## Why

The first reason, is just because I wanted to do it, I wanted to learn primarily how I could interact my local ollama
instance in an automated way (yes I was using curl before), furthermore, I also wanted to improve my Java skills, which
I could see were pretty much dull, not only that, but I kinda figured that Java doesn't have JSON parsers in its
standard library, a shocker from me since I am doing much more stuff in Golang.

Secondly, I like to have my own tooling, it's not like I don't trust the open web ui, but I do prefer to have these kinds
of tools under my control, even if they are way more primitive than the latter. This is one of my main motivator to do
anything, yes, I am reinventing the wheel but at least it's my wheel, if that makes sense.

Thirdly and most importantly, this seemed like a fun little project for me and a way to give another usage for ollama. I
was simply using it as a code completion tool for
my [Neovim config](https://github.com/MiguelFerreira18/neovim.config/blob/main/lua/plugins/ollama.lua), with this I can
easily see my self prompting a model that isn't just a coding model.

## What can it do

Currently, besides allowing for you to give it a port for ollama, you can select a model that you already have installed
and prompt it with any question, if you are done with your "prompting" session, just use the **/exit** to leave the
tool, on the other hand, if you are not happy with the model, you can just call the **/model** command to switch to
another model of your choosing.

Finally, it has a planning command via **/plan**, this injects context into the llm to know that the output should
always be a plan, if you have a better prompt for this case, I would highly recommend to contribute, as I know the one I
developed is flawed. Moreover, the **/save_history** was also implemented, and limited to 20 messages in total, this was
done using the deque dst, this was mainly to prevent the context to grow outside its limit, even now, the limit could
be breached using a malicious prompt.

Whenever **/plan** or **/save_history** are called and the model is changed, the settings will restore to their
defaults,
meaning that in the new model you select, that chat history and planning mode are disabled.

## Planned features

integration with Ollama cloud or any other AI API.

Finally, I was thinking of giving mcp tools for ollama, but currently it doesn't seem that any llm from Ollama can do
things like Gemini cli and Claude code do, or I am totally wrong and there are already some. Either way, I am a bit
skeptical of doing something like this, specifically because of security reasons, yes it's a local model, but nothing
stops it from doing anything wrong and be totally happy about it. So this will remain as food for thought for now.

## Architecture (On standby)

## How to run

```bash
mvn clean install
mvn package
mvn exec:java -Dexe.mainClass="com.codeCLi.Main"
```

or

```bash
mvn clean package
java -jar target/Ollama_chat_tool-1.0-SNAPSHOT.js
```

Because I put shade to bundle the jar with the dependencies the second option will create two jars, execute the one with
just the name of the repo **without** the **original** part, which is the one that is on the command. You can use the
original, but you will have to install the dependencies your self.

## Contributing

Feel free to contribute in any way you want. It can be a feature request, documentation updates, code contribution or
testing.

## License

The license under use is the MIT one, so feel free to use this project whoever you like it.

