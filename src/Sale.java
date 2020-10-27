import java.util.Scanner;

public class Sale {
    public static final double saleCommissionRate = 0.1;
    public static final double levelCommissionRate = 0.1;
    private double price;
    Company company;
    public Sale() {

    }

    public Sale(Company com, double price) {
        company = com;
        this.price = price;
    }

    public void saleResult(Distributor d) { // d should be boss
        if(d == null) return;

        saleResult(d.getLeftLeg());
        saleResult(d.getRightLeg());
        personalSaleResult(d);
    }

    public void personalSaleResult(Distributor d) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Amount of goods sold by " + d.getName() + " is: ");
        int amount = sc.nextInt();
        d.setNumberOfProducts(amount);
    }

    public double pairCommission(Distributor d) {
        if(d.getLeftLeg() == null || d.getRightLeg() == null) return 0;
        if(totalProducts(d.getLeftLeg()) > totalProducts(d.getRightLeg())) {
           return levelCommissionRate * d.getRightLeg().getNumberOfProducts() * price;
        } else return levelCommissionRate * d.getLeftLeg().getNumberOfProducts() * price;
    }


    public double totalProducts(Distributor d) {
        if(d == null) return 0;
        int total = d.getNumberOfProducts();
        total += totalProducts(d.getLeftLeg()) + totalProducts(d.getRightLeg());
        return total;
    }
    public double saleCommission(Distributor d) {
        return d.getNumberOfProducts() * price * saleCommissionRate;
    }

    public double commission(Distributor d) {
        return saleCommission(d) + pairCommission(d);
    }
}
