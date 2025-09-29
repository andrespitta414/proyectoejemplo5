import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        VehicleRepository repo = new VehicleRepository();
        TaxCalculator calc = new TaxCalculator(0.02);
        SwingUtilities.invokeLater(() -> {
            MainFrame f = new MainFrame(repo, calc);
            f.setVisible(true);
        });
    }
}
