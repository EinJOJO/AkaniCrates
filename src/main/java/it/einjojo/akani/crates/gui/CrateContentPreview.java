package it.einjojo.akani.crates.gui;

import it.einjojo.akani.crates.content.CrateContent;
import it.einjojo.akani.crates.crate.Crate;
import mc.obliviate.inventory.ComponentIcon;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.pagination.PaginationManager;
import org.bukkit.entity.Wither;

import java.util.function.Consumer;

public class CrateContentPreview {
    private final PaginationManager paginationManager;
    private final Crate crate;
    private final Gui gui;
    private Consumer<ComponentIcon> previewIconModifier;

    public CrateContentPreview(Crate crate, Gui gui) {
        this.paginationManager = new PaginationManager(gui);
        this.crate = crate;
        this.gui = gui;
    }

    public void placeContents() {
        paginationManager.registerPageSlotsBetween(9, 44);
        for (CrateContent content : crate.contents()) {
            ComponentIcon icon = new Icon(content.previewItem()).toComp();
            if (previewIconModifier != null) {
                previewIconModifier.accept(icon);
            }
            paginationManager.addItem(icon.toIcon());
        }
        paginationManager.update();
        addPreviousPage();
        addNextPage();
    }

    private void addNextPage() {
        gui.addItem(47, new Icon(Heads.RIGHT.skull()).toComp()
                .onClick((event) -> {
                    if (!paginationManager.isLastPage()) {
                        paginationManager.goNextPage();
                        paginationManager.update();
                    }
                })
        );
    }

    private void addPreviousPage() {
        gui.addItem(46, new Icon(Heads.LEFT.skull()).toComp()
                .onClick((event) -> {
                    if (!paginationManager.isFirstPage()) {
                        paginationManager.goPreviousPage();

                    }
                })
        );
    }
}
