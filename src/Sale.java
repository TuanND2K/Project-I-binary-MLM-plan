import java.util.Scanner;

public class Sale {
    public static final double saleCommissionRate = 0.1;
    public static final double levelCommissionRate = 0.1;
    private double price;
    Branch branch;

    public Sale() {

    }

    public Sale(Branch b, double price) {
        branch = b;
        this.price = price;
        setNewSale(b.getBoss());
    }

    public void setNewSale(Distributor boss) {
        if(boss == null) return;
        boss.setProductsSold(0);
        setNewSale(boss.getLeftLeg());
        setNewSale(boss.getRightLeg());
    }

//    public void saleResult(Distributor d) { // d should be boss
//        if(d == null) return;
//        saleResult(d.getLeftLeg());
//        saleResult(d.getRightLeg());
//        updateSaleResult(d);
//    }

    /**
     * cap nhat so hang ban duoc cua 1 nha phan phoi
     */
    public void updateSaleResult(Distributor d) {
        Scanner sc = new Scanner(System.in);
        System.out.print(d.getName() + " đã bán được thêm: ");
        int amount = sc.nextInt();
        int curr = d.getProductsSold();
        d.setProductsSold(curr + amount);
    }

    /**
     * hoa hong thu duoc tu viec ban hang cua cac nha phan phoi cap duoi
     */
    public double pairCommission(Distributor d) {
        if(d.getLeftLeg() == null || d.getRightLeg() == null) return 0;
        if(totalProducts(d.getLeftLeg()) > totalProducts(d.getRightLeg())) {
           return levelCommissionRate * totalProducts(d.getRightLeg()) * price;
        } else return levelCommissionRate * totalProducts(d.getLeftLeg()) * price;
    }

    /**
     * tong so hang ban duoc den hien tai cua 1 nha phan phoi
     */
    public double totalProducts(Distributor d) {
        if(d == null) return 0;
        int total = d.getProductsSold();
        total += totalProducts(d.getLeftLeg()) + totalProducts(d.getRightLeg());
        return total;
    }

    /**
     * hoa hong thu duoc truc tiep tu viec ban hang
     */
    public double saleCommission(Distributor d) {
        return d.getProductsSold() * price * saleCommissionRate;
    }

    /**
     * tong hoa hong duoc nhan cua 1 nha phan phoi
     */
    public void commission(Distributor d) {
        d.setCommission(saleCommission(d) + pairCommission(d));
    }
}
