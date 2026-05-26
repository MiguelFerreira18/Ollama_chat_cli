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

Secondly, I like to have my own tooling, it not like I don't trust the open web ui, but I do prefer to have these kinds
of tools under my control, even if they are way more primitive than the latter. This is one of my main motivator to do
anything, yes, I am reinventing the wheel but at least it's my wheel, if that makes sense.

Thirdly and most importantly, this seemed like a fun little project for me and a way to give another usage for ollama. I
was simply using it as a code completion tool for
my [Neovim config](https://github.com/MiguelFerreira18/neovim.config/blob/main/lua/plugins/ollama.lua), with this I can
easily see my self prompting a model
that isn't just a coding model.

## What can it do

Currently, besides allowing for you to give it a port for ollama, you can select a model that you already have installed
and prompt it with any question, if you are done with your "prompting" session, just use the **/exit** to leave the
tool, on the other hand, if you are not happy with the model, you can just call the **/model** command to switch to
another model of your choosing.

## Planned features

There are two commands that I want implemented, the **/plan** command, which gives context into the llm into creating a
plan based on your prompt to it. The second command that I want to implement is a context history command like
**/save_history**, this won't save any of the chats, but instead will save, for the remaining of the current open chat
, the conversations. So if the user is talking to llm A and activates that command, all the chats with llm A are saved
until you either, exit the program, or switch to another llm. This is intentional, if you want a tool actually save the
chats and their context, I suggest you to do one of two things, fork this and implement it your self, if the
implementation makes sense, I can merge I without a problem, if that is not ok, you can just use open web ui or any
other tool out there, which is probably advisable.

Another feature, is integration with Ollama cloud or any other AI API.

Finally, I was thinking of giving mcp tools for ollama, but currently it doesn't seem that any llm from Ollama can do
things like Gemini cli and Claude code do, or I am totally wrong and there are already some. Either way, I am a bit
skeptical of doing something like this, specifically because of security reasons, yes it's a local model, but nothing
stops it from doing anything wrong and be totally happy about it. So this will remain as food for thought for now.

## Architecture (On standby)

## Contributing

Feel free to contribute in any way you want. It can be a feature request, documentation updates, code contribution or
testing.

## License

The license under use is the MIT one, so feel free to use this project whoever you like it.

