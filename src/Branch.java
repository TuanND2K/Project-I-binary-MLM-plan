import javax.swing.*;
import java.util.ArrayList;

/**
 * 1 Branch(chi nhanh cua 1 cong ty) la 1 cay nhi phan
 */
public class Branch {
    private final int ID;
    private static int IDset = 0;
    private Distributor manager;
    private String name;
    private ArrayList<Distributor> memberList;
    public Distributor getManager() {
        return manager;
    }

    public Branch(Distributor boss, String name) {
        IDset++;
        ID = IDset;
        this.manager = boss;
        this.name = name;
        memberList = new ArrayList<>();
        memberList.add(boss);
    }

    public int getID() {
        return ID;
    }
    public String getName() {return name;}
    public ArrayList<Distributor> getMemberList() {
        return memberList;
    }

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
                add(newMember, sponsor.getRightLeg());
            else {
                add(newMember, sponsor.getLeftLeg());
            }
        }
    }
    public void addDistributor(Distributor newMember, Distributor sponsor) {
        memberList.add(newMember);
        add(newMember, sponsor);
        newMember.setBranchID(sponsor.getBranchID());
        newMember.setSponsor(sponsor);
    }

    public boolean checkForAdd(Distributor sponsor) {
        while(sponsor != null) {
            if(!checkAdd(sponsor))
                return false;
            sponsor = sponsor.getParent();
        }
        return true;
    }

    private boolean checkAdd(Distributor sponsor) {
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
        return Distributor.find(manager, ID);
    }

    /**
     *xoa nha phan phoi khoi cay nhi phan
     */
    public void deleteDistributor(Distributor d) {
        memberList.remove(d);
        Distributor toPromote = Distributor.distributorToPromote(d);
        if(toPromote == null) {
            return;
        }
        Distributor notPromote = (d.getLeftLeg() == toPromote) ? d.getRightLeg() : d.getLeftLeg();
        Distributor parent = d.getParent();
        if(parent != null) {
            if(parent.getRightLeg() == d) {
                parent.setRightLeg(toPromote);
            } else {
                parent.setLeftLeg(toPromote);
            }
            toPromote.setParent(parent);
        } else {
            manager = toPromote;
            Main.updateManager(this);
            toPromote.setParent(null);
        }
        Main.updateParent(toPromote);
        if(notPromote != null) reorder(toPromote, notPromote);
    }

    /**
     * sap xep lai thu tu cac node sau khi xoa 1 nha phan phoi
     */
    private void reorder(Distributor d, Distributor e) {
        System.out.println("here");
        if(d.getLeftLeg() == null) {
            d.setLeftLeg(e);
            e.setParent(d);
            Main.updateParent(e);
        }
        else if(d.getRightLeg() == null) {
            d.setRightLeg(e);
            e.setParent(d);
            Main.updateParent(e);
        }
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
