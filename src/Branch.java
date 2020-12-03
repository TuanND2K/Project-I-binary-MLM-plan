import java.io.File;
import java.util.ArrayList;

/**
 * 1 Branch(chi nhanh cua 1 cong ty) la 1 cay nhi phan
 */
public class Branch {
    private Distributor boss;
    private String name;
    private ArrayList<Distributor> memberList;
    private File storeFile;
    public Distributor getBoss() {
        return boss;
    }

    public Branch() {

    }
    public Branch(Distributor boss, String name) {
        this.boss = boss;
        this.name = name;
        memberList = new ArrayList<>();
        memberList.add(boss);
        storeFile = StoreData.makeFile(this);
    }

    public ArrayList<Distributor> getMemberList() {
        return memberList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name = name;}

    private void add(Distributor newMember, Distributor sponsor) {
        if(sponsor.getParent() != null) {
            Distributor p = sponsor.getParent();
            if(p.getLeftLeg() == null) {
                p.setLeftLeg(newMember);
                newMember.setParent(p);
                return;
            }
            if(p.getRightLeg() == null) {
                p.setRightLeg(newMember);
                newMember.setParent(p);
                return;
            }
        }
        if(newMember.getSponsor() == null) newMember.setSponsor(sponsor);
        if(sponsor.getLeftLeg() == null) {
            sponsor.setLeftLeg(newMember);
            newMember.setParent(sponsor);
        }
        else if(sponsor.getRightLeg() == null) {
            sponsor.setRightLeg((newMember));
            newMember.setParent(sponsor);
        }
        else {
            if(sponsor.getLeftLeg().numberOfInferiors() > sponsor.getRightLeg().numberOfInferiors())
                addDistributor(newMember, sponsor.getRightLeg());
            else {
                addDistributor(newMember, sponsor.getLeftLeg());
            }
        }
    }
    public void addDistributor(Distributor newMember, Distributor sponsor) {
        if(!checkForAdd(sponsor)) {
            System.out.println("Không thể thêm do chênh lệch lớn");
        }
        else {
            memberList.add(newMember);
            add(newMember, sponsor);
            StoreData.addInfor(storeFile, newMember);
        }
    }

    public boolean checkForAdd(Distributor sponsor) {
        if(sponsor.getParent() == null) return true;
        Distributor p = sponsor.getParent();
        int a1 = sponsor.numberOfInferiors();
        int a2;
        if(p.getRightLeg() == null || p.getLeftLeg() == null) a2 = 0;
        else if(p.getRightLeg() != sponsor) a2 = p.getRightLeg().numberOfInferiors();
        else a2 = p.getLeftLeg().numberOfInferiors();
        final int MAX_DISPARITY = 4;
        return a1 - a2 < MAX_DISPARITY;
    }
    /**
     *tim nha phan phoi voi ID cho truoc trong chi nhanh
     */
    public Distributor find(int ID) {
        return Distributor.find(boss, ID);
    }

    /**
     *xoa nha phan phoi khoi cay nhi phan
     */
    public void deleteDistributor(Distributor d) {
        memberList.remove(d);
        Distributor toPromote = Distributor.distributorToPromote(d);
        Distributor notPromote = (d.getLeftLeg() == toPromote) ? d.getRightLeg() : d.getLeftLeg();
        Distributor parent = d.getParent();
        if(parent != null) {
            if(parent.getRightLeg() == d) {
                parent.setRightLeg(toPromote);
            } else parent.setLeftLeg(toPromote);
        }
        if(toPromote != null) reorder(toPromote, notPromote);
    }

    /**
     * sap xep lai thu tu cac node sau khi xoa 1 nha phan phoi
     */
    private void reorder(Distributor d, Distributor e) {
        if(e == null) return;
        if(d.getLeftLeg() == null) d.setLeftLeg(e);
        else if(d.getRightLeg() == null) d.setRightLeg(e);
        else {
            if(d.getRightLeg().getCommission() > d.getLeftLeg().getCommission()) {
                Distributor old_left = d.getLeftLeg();
                d.setLeftLeg(e);
                reorder(e, old_left);
            } else {
                Distributor old_right = d.getRightLeg();
                d.setRightLeg(e);
                reorder(e, old_right);
            }
        }
    }
}
