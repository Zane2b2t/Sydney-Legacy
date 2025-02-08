package me.aidan.sydney.commands.impl;

import me.aidan.sydney.Sydney;
import me.aidan.sydney.commands.Command;
import me.aidan.sydney.commands.RegisterCommand;
import me.aidan.sydney.modules.Module;
import me.aidan.sydney.utils.chat.ChatUtils;
import me.aidan.sydney.utils.input.KeyboardUtils;

import java.util.List;

@RegisterCommand(name = "bind", tag = "Bind", description = "Changes the toggle keybind of a module.", syntax = "<[module]> <[key]|reset> | <reset|list> ", aliases = {"b", "key", "keybind"})
public class BindCommand extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            Module module = Sydney.MODULE_MANAGER.getModule(args[0]);
            if (module == null) {
                Sydney.CHAT_MANAGER.tagged("Could not find the module specified.", getTag(), getName());
                return;
            }

            if (args[1].equalsIgnoreCase("reset")) {
                module.setBind(0);
                Sydney.CHAT_MANAGER.tagged("Successfully reset the toggle keybind of the " + ChatUtils.getPrimary() + module.getName() + ChatUtils.getSecondary() + " module.", getTag(), getName());
            } else {
                module.setBind(KeyboardUtils.getKeyNumber(args[1]));
                Sydney.CHAT_MANAGER.tagged("Successfully bound the " + ChatUtils.getPrimary() + module.getName() + ChatUtils.getSecondary() + " module to the " + ChatUtils.getPrimary() + KeyboardUtils.getKeyName(module.getBind()) + ChatUtils.getSecondary() + " key.", getTag(), getName());
            }
        } else if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "reset" -> {
                    Sydney.MODULE_MANAGER.getModules().forEach(m -> m.bind.resetValue());
                    Sydney.CHAT_MANAGER.tagged("Successfully reset every module's toggle keybind.", getTag(), getName());
                }
                case "list" -> {
                    List<Module> modules = Sydney.MODULE_MANAGER.getModules().stream().filter(m -> m.getBind() != 0).toList();

                    if (modules.isEmpty()) {
                        Sydney.CHAT_MANAGER.tagged("There are currently no bound modules.", getTag(), getName() + "-list");
                    } else {
                        StringBuilder builder = new StringBuilder();
                        int index = 0;

                        for (Module module : modules) {
                            index++;
                            builder.append(ChatUtils.getSecondary()).append(module.getName())
                                    .append(ChatUtils.getPrimary()).append(" [")
                                    .append(ChatUtils.getSecondary()).append(KeyboardUtils.getKeyName(module.getBind()).toUpperCase())
                                    .append(ChatUtils.getPrimary()).append("]")
                                    .append(index == modules.size() ? "" : ", ");
                        }

                        Sydney.CHAT_MANAGER.message("Bound Modules " + ChatUtils.getPrimary() + "[" + ChatUtils.getSecondary() + modules.size() + ChatUtils.getPrimary() + "]: " + ChatUtils.getSecondary() + builder, getName() + "-list");
                    }
                }
                default -> messageSyntax();
            }
        } else {
            messageSyntax();
        }
    }
}
