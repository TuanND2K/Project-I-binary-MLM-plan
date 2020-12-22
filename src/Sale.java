public class Sale {
    public static int order = 1;
    public static final double saleCommissionRate = 0.1;
    public static final double levelCommissionRate = 0.1;
    private double price;
    Branch branch;

    public Sale(Branch b, double price) {
        branch = b;
        this.price = price;
        reset();
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder() {
        order++;
    }
    /**
     * hoa hong thu duoc tu viec ban hang cua cac nha phan phoi cap duoi
     */
    public double pairCommission(Distributor d) {
        if(d.getLeftLeg() == null || d.getRightLeg() == null) return 0;
        if(totalProducts(d.getLeftLeg()) > totalProducts(d.getRightLeg())) {
           return levelCommissionRate * d.getRightLeg().getCommission();
        } else return levelCommissionRate * d.getLeftLeg().getCommission();
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

    public void reset() {
        for(Distributor d: branch.getMemberList()) {
            d.setProductsSold(0);
        }
    }
}
