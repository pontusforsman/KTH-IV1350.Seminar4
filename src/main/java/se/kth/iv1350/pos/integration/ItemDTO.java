package se.kth.iv1350.pos.integration;

/**
 * Data Transfer Object for an item, used to transfer item data between layers.
 */
public record ItemDTO(String id, String name, String description, double price, double vatRate) {

} 