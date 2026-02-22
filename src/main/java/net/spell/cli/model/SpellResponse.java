package net.spell.cli.model;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpellResponse {
    List<Spell> spellList;

}
