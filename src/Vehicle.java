public class Vehicle {
    private String marca;
    private String linea;
    private int año;
    private double valorComercial;

    public Vehicle(String marca, String linea, int año, double valorComercial) {
        this.marca = marca;
        this.linea = linea;
        this.año = año;
        this.valorComercial = valorComercial;
    }

    public String getMarca() { return marca; }
    public String getLinea() { return linea; }
    public int getAño() { return año; }
    public double getValorComercial() { return valorComercial; }

    @Override
    public String toString() {
        return marca + " " + linea + " (" + año + ") - " + valorComercial;
    }
}

