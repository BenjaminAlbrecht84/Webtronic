package model.warehouse;

public class Item {

    private String identifier;

    public Item(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Item)
            return ((Item) obj).getIdentifier().equals(identifier);
        return false;
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
