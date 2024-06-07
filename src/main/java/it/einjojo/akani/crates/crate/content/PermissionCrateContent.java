package it.einjojo.akani.crates.crate.content;

/**
 * Crate content that grants a permission to the player
 */
public interface PermissionCrateContent extends CrateContent {

    /**
     * Permission that will be granted to the player
     *
     * @return the permission
     */
    String permission();

    /**
     * Describes what is possible with the permission
     *
     * @return the description
     */
    String permissionDescription();

}
