import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private VehicleRepository repo;
    private TaxCalculator calc;

    private JComboBox<String> cbMarca = new JComboBox<>();
    private JComboBox<String> cbLinea = new JComboBox<>();
    private JComboBox<Integer> cbAño = new JComboBox<>();
    private JTextField tfValor = new JTextField(10);

    private JCheckBox chkPronto = new JCheckBox("Pronto pago (%)");
    private JTextField tfPctPronto = new JTextField("5", 4);
    private JCheckBox chkServicio = new JCheckBox("Servicio público (valor fijo)");
    private JTextField tfValServicio = new JTextField("5000", 6);
    private JCheckBox chkTraslado = new JCheckBox("Traslado cuenta (%)");
    private JTextField tfPctTraslado = new JTextField("10", 4);

    private JTextArea taResultado = new JTextArea(8, 40);

    public MainFrame(VehicleRepository repo, TaxCalculator calc) {
        super("Calculadora impuesto vehicular");
        this.repo = repo;
        this.calc = calc;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 420);
        setLocationRelativeTo(null);

        tfValor.setEditable(false);

        // Panel superior
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (String m : repo.getMarcas()) cbMarca.addItem(m);
        cbMarca.insertItemAt("-- Seleccione --", 0);
        cbMarca.setSelectedIndex(0);

        top.add(new JLabel("Marca:")); top.add(cbMarca);
        top.add(new JLabel("Línea:")); top.add(cbLinea);
        top.add(new JLabel("Año:")); top.add(cbAño);
        top.add(new JLabel("Valor comercial:")); top.add(tfValor);

        // --- Eventos ---
        cbMarca.addActionListener(e -> {
            cbLinea.removeAllItems();
            cbAño.removeAllItems();
            String marca = (String) cbMarca.getSelectedItem();
            if (marca == null || marca.startsWith("--")) return;
            for (String l : repo.getLineasByMarca(marca)) cbLinea.addItem(l);
        });

        cbLinea.addActionListener(e -> {
            cbAño.removeAllItems();
            String marca = (String) cbMarca.getSelectedItem();
            String linea = (String) cbLinea.getSelectedItem();
            if (marca == null || linea == null || marca.startsWith("--")) return;
            for (Integer a : repo.getAñosByMarcaLinea(marca, linea)) cbAño.addItem(a);
        });

        cbAño.addActionListener(e -> {
            String marca = (String) cbMarca.getSelectedItem();
            String linea = (String) cbLinea.getSelectedItem();
            Integer anio = (Integer) cbAño.getSelectedItem();
            if (marca == null || linea == null || anio == null) return;

            var opt = repo.findVehicle(marca, linea, anio);
            if (opt.isPresent()) {
                tfValor.setText(String.valueOf(opt.get().getValorComercial()));
            } else {
                tfValor.setText("");
            }
        });

        // Panel de descuentos
        JPanel discounts = new JPanel(new FlowLayout(FlowLayout.LEFT));
        discounts.add(chkPronto); discounts.add(tfPctPronto);
        discounts.add(chkServicio); discounts.add(tfValServicio);
        discounts.add(chkTraslado); discounts.add(tfPctTraslado);

        JButton btnCalcular = new JButton("Calcular impuesto");
        btnCalcular.addActionListener(e -> onCalcular());

        taResultado.setEditable(false);

        JPanel center = new JPanel(new BorderLayout());
        center.add(discounts, BorderLayout.NORTH);
        center.add(btnCalcular, BorderLayout.CENTER);
        center.add(new JScrollPane(taResultado), BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    private void onCalcular() {
        try {
            String marca = (String) cbMarca.getSelectedItem();
            String linea = (String) cbLinea.getSelectedItem();
            Integer año = (Integer) cbAño.getSelectedItem();
            if (marca == null || linea == null || año == null) {
                JOptionPane.showMessageDialog(this, "Selecciona marca, línea y año", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var opt = repo.findVehicle(marca, linea, año);
            if (opt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vehículo no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Vehicle v = opt.get();

            boolean p = chkPronto.isSelected();
            boolean s = chkServicio.isSelected();
            boolean t = chkTraslado.isSelected();

            double pctPronto = p ? Double.parseDouble(tfPctPronto.getText().trim()) : 0;
            double valServicio = s ? Double.parseDouble(tfValServicio.getText().trim()) : 0;
            double pctTraslado = t ? Double.parseDouble(tfPctTraslado.getText().trim()) : 0;

            TaxResult r = calc.calcular(v, p, pctPronto, s, valServicio, t, pctTraslado);

            StringBuilder sb = new StringBuilder();
            sb.append("Base (valor comercial): ").append(r.getBaseValue()).append("\n");
            sb.append("Después pronto pago: ").append(r.getAfterPronto()).append("\n");
            sb.append("Después servicio público: ").append(r.getAfterServicio()).append("\n");
            sb.append("Después traslado: ").append(r.getAfterTraslado()).append("\n");
            sb.append("Total descuentos: ").append(r.getTotalDiscount()).append("\n");
            sb.append("Impuesto a pagar (").append(calc.impuestoPorcentaje * 100).append("%): ").append(r.getTaxAmount()).append("\n");

            taResultado.setText(sb.toString());
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Datos numéricos inválidos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
