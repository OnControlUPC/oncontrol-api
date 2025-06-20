package pe.edu.upc.oncontrol.appointment.domain.model.valueobject;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class Location implements Serializable {

    private final String name;
    private final String mapsUrl;

    public Location(String name, String mapsUrl) {
        if ((name == null || name.isBlank()) && (mapsUrl == null || mapsUrl.isBlank())) {
            throw new IllegalArgumentException("Location must have at least a name or a mapsUrl.");
        }

        if (mapsUrl != null && !mapsUrl.isBlank()) {
            try {
                URI uri = new URI(mapsUrl);
                if (uri.getScheme() == null || uri.getHost() == null) {
                    throw new IllegalArgumentException("mapsUrl must have a valid scheme and host.");
                }
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid mapsUrl.");
            }
        }

        this.name = name != null ? name.trim() : null;
        this.mapsUrl = mapsUrl != null ? mapsUrl.trim() : null;
    }

    public String getName() {
        return name;
    }

    public String getMapsUrl() {
        return mapsUrl;
    }

    public boolean isPresent(){
        return (name != null && !name.isBlank()) || (mapsUrl != null && !mapsUrl.isBlank());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location that)) return false;
        return Objects.equals(name, that.name) &&
                Objects.equals(mapsUrl, that.mapsUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mapsUrl);
    }
}