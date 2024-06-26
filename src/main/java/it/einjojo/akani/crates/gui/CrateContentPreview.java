package it.einjojo.akani.crates.gui;

import it.einjojo.akani.crates.crate.Crate;
import it.einjojo.akani.crates.crate.content.CrateContent;
import mc.obliviate.inventory.ComponentIcon;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.pagination.PaginationManager;

import java.util.function.BiConsumer;

public class CrateContentPreview {
    private final PaginationManager paginationManager;
    private final Crate crate;
    private final Gui gui;
    private BiConsumer<CrateContent, ComponentIcon> previewIconModifier;

    public CrateContentPreview(Crate crate, Gui gui) {
        this.paginationManager = new PaginationManager(gui);
        paginationManager.registerPageSlotsBetween(9, 44);
        this.crate = crate;
        this.gui = gui;
    }

    public void placeContents() {
        paginationManager.getItems().clear();
        for (CrateContent content : crate.contents()) {
            ComponentIcon icon = new Icon(content.previewItem().clone()).toComp();
            if (previewIconModifier != null) {
                previewIconModifier.accept(content, icon);
            }
            paginationManager.addItem(icon.toIcon());
        }
        paginationManager.update();
        addPreviousPage();
        addNextPage();
    }

    public PaginationManager paginationManager() {
        return paginationManager;
    }

    public Crate crate() {
        return crate;
    }

    public Gui gui() {
        return gui;
    }


    public void setPreviewIconModifier(BiConsumer<CrateContent, ComponentIcon> previewIconModifier) {
        this.previewIconModifier = previewIconModifier;
    }

    private void addNextPage() {
        gui.addItem(47, new Icon(Heads.RIGHT.skull()).toComp()
                .onClick((event) -> {
                    if (!paginationManager.isLastPage()) {
                        paginationManager.goNextPage();
                        paginationManager.update();
                        GuiSound.GOOD_CLICK.play(gui);
                    } else {
                        GuiSound.BAD_CLICK.play(gui);
                    }
                })
        );
    }

    private void addPreviousPage() {
        gui.addItem(46, new Icon(Heads.LEFT.skull()).toComp()
                .onClick((event) -> {
                    if (!paginationManager.isFirstPage()) {
                        paginationManager.goPreviousPage();
                        paginationManager.update();
                        GuiSound.GOOD_CLICK.play(gui);
                    } else {
                        GuiSound.BAD_CLICK.play(gui);
                    }
                })
        );
    }
}
