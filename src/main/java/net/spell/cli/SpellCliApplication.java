package net.spell.cli;

import net.spell.cli.model.Spell;
import net.spell.cli.model.SpellResponse;
import net.spell.cli.util.CSVReaderUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.shell.core.command.CommandArgument;
import org.springframework.shell.core.command.CommandContext;
import org.springframework.shell.core.command.annotation.Command;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SpellCliApplication {

    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    static void main(String[] args) {
        SpringApplication.run(SpellCliApplication.class, args);
    }


    @Command(description = "Convert from csv to json.", group = "CSV")
    public String convert(CommandContext commandContext) {
        CommandArgument sourceFile = commandContext.getArgumentByIndex(0);
        CommandArgument targetPath = commandContext.getArgumentByIndex(1);
        if(sourceFile == null) {
            return "No source file provided";
        }

        Path source, target;
        List<Spell> spellList;
        String fileName;
        if(!StringUtils.isEmpty(sourceFile.value())) {
            source = Paths.get(sourceFile.value()).toAbsolutePath();
            if(Files.exists(source)) {
                fileName = source.getFileName().toString();

                if(!fileName.endsWith(".csv")) {
                    return "Source file is not a valid CSV file";
                }


                if(targetPath == null) {
                    target = source.toAbsolutePath();
                } else {
                    target = Paths.get(targetPath.value()).toAbsolutePath().resolve(source.getFileName());
                }

                target = target.resolveSibling(fileName.replace(".csv", ".json"));

                try {
                    spellList = CSVReaderUtil.readCSV(source.toString(), Spell.class);
                } catch (FileNotFoundException e) {
                    return "Source file is not a valid CSV file";
                }

                if(!spellList.isEmpty()) {
                    SpellResponse spellResponse = new SpellResponse();
                    spellResponse.setSpellList(spellList);
                    ow.writeValue(target, spellResponse);
                    return "Finished converting";
                }
            } else {
                return "Source path does not exist";
            }
        }
        return "Hello World!";
    }
}
