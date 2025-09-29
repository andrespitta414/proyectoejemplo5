public class TaxResult {
    private double baseValue;
    private double afterPronto;
    private double afterServicio;
    private double afterTraslado;
    private double totalDiscount;
    private double taxAmount;

    public TaxResult(double baseValue, double afterPronto, double afterServicio, double afterTraslado, double totalDiscount, double taxAmount) {
        this.baseValue = baseValue;
        this.afterPronto = afterPronto;
        this.afterServicio = afterServicio;
        this.afterTraslado = afterTraslado;
        this.totalDiscount = totalDiscount;
        this.taxAmount = taxAmount;
    }

    public double getBaseValue() { return baseValue; }
    public double getAfterPronto() { return afterPronto; }
    public double getAfterServicio() { return afterServicio; }
    public double getAfterTraslado() { return afterTraslado; }
    public double getTotalDiscount() { return totalDiscount; }
    public double getTaxAmount() { return taxAmount; }
}
