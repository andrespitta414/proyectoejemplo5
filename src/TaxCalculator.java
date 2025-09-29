import java.text.DecimalFormat;

public class TaxCalculator {
    protected double impuestoPorcentaje = 0.02;

    public TaxCalculator(double impuestoPorcentaje) {
        this.impuestoPorcentaje = impuestoPorcentaje;
    }

    public TaxResult calcular(Vehicle v,
                              boolean aplicaProntoPago, double pctProntoPago,
                              boolean aplicaServicioPublico, double valorServicioPublico,
                              boolean aplicaTrasladoCuenta, double pctTraslado) throws IllegalArgumentException {
        double baseValue = v.getValorComercial();

        if (aplicaProntoPago && aplicaTrasladoCuenta && pctTraslado <= pctProntoPago) {
            throw new IllegalArgumentException("El porcentaje de traslado debe ser mayor que el porcentaje de pronto pago.");
        }

        // Paso 1: Pronto pago
        double afterPronto = baseValue;
        if (aplicaProntoPago) {
            afterPronto = baseValue * (1 - pctProntoPago / 100.0);
        }

        // Paso 2: Servicio público
        double afterServicio = afterPronto;
        if (aplicaServicioPublico) {
            afterServicio = afterPronto - valorServicioPublico;
            if (afterServicio < 0) afterServicio = 0;
        }

        // Paso 3: Traslado de cuenta
        double afterTraslado = afterServicio;
        if (aplicaTrasladoCuenta) {
            afterTraslado = afterServicio * (1 - pctTraslado / 100.0);
        }

        // Calcular descuentos totales y el impuesto
        double totalDiscount = baseValue - afterTraslado;
        if (totalDiscount < 0) totalDiscount = 0; // seguridad

        double taxAmount = afterTraslado * impuestoPorcentaje;

        return new TaxResult(baseValue, afterPronto, afterServicio, afterTraslado, totalDiscount, taxAmount);
    }
}

