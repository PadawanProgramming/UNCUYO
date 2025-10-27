package easystore.service;

import easystore.dao.PurchaseDAO;
import easystore.dao.SaleDAO;

public class FinancesService implements Reportable {

    private final PurchaseDAO purchaseDAO;
    private final SaleDAO saleDAO;

    public FinancesService(PurchaseDAO purchaseDAO, SaleDAO saleDAO) {
        this.purchaseDAO = purchaseDAO;
        this.saleDAO = saleDAO;
    }

    public double totalExpenses() {
        return purchaseDAO.readAll().stream().mapToDouble(p -> p.getTotalAmount()).sum();
    }

    public double totalIncomes() {
        return saleDAO.readAll().stream().mapToDouble(s -> s.getTotalAmount()).sum();
    }

    public double getBalance() {
        return totalIncomes() - totalExpenses();
    }

    @Override
    public void generateReport() {
        System.out.println("--- Reporte de Finanzas ---");
        System.out.println("Ingresos Totales: " + totalIncomes());
        System.out.println("Gastos Totales: " + totalExpenses());
        System.out.println("Balance: " + getBalance());
    }
}
