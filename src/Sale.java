import java.util.Scanner;

public class Sale {
    public static final double saleCommissionRate = 0.1;
    public static final double levelCommissionRate = 0.1;
    private double price;
    Company company;
    public Sale() {

    }

    // thiet lap dot phan phoi moi
    public Sale(Company com, double price) {
        company = com;
        this.price = price;
        setNewSale(com.getBoss());
    }

    public void setNewSale(Distributor boss) {
        if(boss == null) return;
        boss.setNumberOfProducts(0);
        setNewSale(boss.getLeftLeg());
        setNewSale(boss.getRightLeg());
    }

    public void saleResult(Distributor d) { // d should be boss
        if(d == null) return;
        saleResult(d.getLeftLeg());
        saleResult(d.getRightLeg());
        personalSaleResult(d);
    }

    public void personalSaleResult(Distributor d) {
        Scanner sc = new Scanner(System.in);
        System.out.print(d.getName() + " : ");
        int amount = sc.nextInt();
        d.setNumberOfProducts(amount);
    }

    public double pairCommission(Distributor d) {
        if(d.getLeftLeg() == null || d.getRightLeg() == null) return 0;
        if(totalProducts(d.getLeftLeg()) > totalProducts(d.getRightLeg())) {
           return levelCommissionRate * d.getRightLeg().getNumberOfProducts() * price;
        } else return levelCommissionRate * d.getLeftLeg().getNumberOfProducts() * price;
    }

    public double totalProducts(Distributor d) { //O(n)
        if(d == null) return 0;
        int total = d.getNumberOfProducts();
        total += totalProducts(d.getLeftLeg()) + totalProducts(d.getRightLeg());
        return total;
    }

    public double saleCommission(Distributor d) {
        return d.getNumberOfProducts() * price * saleCommissionRate;
    }

    public void commission(Distributor d) {
        d.setCommission(saleCommission(d) + pairCommission(d));
    }
}
