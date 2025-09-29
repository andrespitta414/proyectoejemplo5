import java.util.*;

public class VehicleRepository {
    private List<Vehicle> vehicles = new ArrayList<>();

    public VehicleRepository() {
        // Datos de ejemplo — amplía o carga desde CSV/DB
        vehicles.add(new Vehicle("Ford", "Taurus", 2018, 40000));
        vehicles.add(new Vehicle("Ford", "Focus", 2016, 25000));
        vehicles.add(new Vehicle("Toyota", "Corolla", 2018, 60000));
        vehicles.add(new Vehicle("Toyota", "Yaris", 2015, 20000));
        vehicles.add(new Vehicle("Chevrolet", "Spark", 2019, 18000));
    }

    public Set<String> getMarcas() {
        Set<String> m = new TreeSet<>();
        for (Vehicle v : vehicles) m.add(v.getMarca());
        return m;
    }

    public Set<String> getLineasByMarca(String marca) {
        Set<String> s = new TreeSet<>();
        for (Vehicle v : vehicles) if (v.getMarca().equalsIgnoreCase(marca)) s.add(v.getLinea());
        return s;
    }

    public Set<Integer> getAñsByMarcaLinea(String marca, String linea) {
        Set<Integer> s = new TreeSet<>();
        for (Vehicle v : vehicles) {
            if (v.getMarca().equalsIgnoreCase(marca) && v.getLinea().equalsIgnoreCase(linea)) {
                s.add(v.getAño());
            }
        }
        return s;
    }

    public Optional<Vehicle> findVehicle(String marca, String linea, int año) {
        return vehicles.stream()
                .filter(v -> v.getMarca().equalsIgnoreCase(marca)
                        && v.getLinea().equalsIgnoreCase(linea)
                        && v.getAño() == año)
                .findFirst();
    }

    public Set<Integer> getAñosByMarcaLinea(String marca, String linea) {
        Set<Integer> s = new TreeSet<>();
        for (Vehicle v : vehicles) {
            if (v.getMarca().equalsIgnoreCase(marca) && v.getLinea().equalsIgnoreCase(linea)) {
                s.add(v.getAño());
            }
        }
        return s;
    }

}
