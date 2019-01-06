package com.wbigelow.ancillary.modules;

import com.google.common.collect.ImmutableList;
import com.wbigelow.ancillary.Command;
import com.wbigelow.ancillary.Module;
import com.wbigelow.ancillary.PermissionLevel;
import lombok.NoArgsConstructor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.List;

/**
 * @author Austin (austinj9#5465)
 */
@NoArgsConstructor
public class MeetupsPlusModule implements Module {

    private static final long MEMBERS_PLUS_CHANNEL = -1L;
    private static final long MEMBERS_PLUS_ROLE = -1L;

    @Override
    public List<Command> getCommands() {
        return ImmutableList.of(new GrantMeetupsPlusCommand());
    }

    @NoArgsConstructor
    final class GrantMeetupsPlusCommand implements Command {

        @Override
        public String getName() {
            return "givemeetups+";
        }

        @Override
        public String getDescription() {
            return "Allows a person with Meetups+ to give Meetups+ to another trusted person.";
        }

        @Override
        public PermissionLevel getRequiredPermissionLevel() {
            return PermissionLevel.MEETUPS_PLUS;
        }

        @Override
        public void execute(final Message message, final DiscordApi discordApi) {
            List<User> mentioned = message.getMentionedUsers();
            if (mentioned.size() == 0) {
                message.getChannel().sendMessage("Missing user mention.");   // todo: write a better error msg
                return;
            }

            final User added = mentioned.get(0);
            final Role role = discordApi.getRoleById(MEMBERS_PLUS_ROLE).get();
            final Server server = message.getServer().get();
            if (added.getRoles(server).contains(role)) {
                message.getChannel().sendMessage("That user already has the role.");
            } else {
                added.addRole(role);
                final MessageAuthor author = message.getAuthor();
                new MessageBuilder()
                        .append(author.getDisplayName() + " added " + added.getDisplayName(server) + "!")
                        .send(discordApi.getChannelById(MEMBERS_PLUS_CHANNEL).get().asTextChannel().get());
            }
        }
    }
}
