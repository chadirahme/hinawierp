package hba;

import model.*;

import java.util.List;

/**
 * Created by chadirahme on 2020-05-30.
 */
public class  VATCodeOperation {

    //static CompSetupModel compSetup;
    public static void getVatAmount(CompSetupModel compSetup,ExpensesModel row) {
        if (compSetup.getUseVAT().equals("Y")) {
            if (row.getSelectedVatCode().getVatPercentage() > 0 && row.getSelectedVatCode().getVatType().equals("T")) {
                //Math.round(row.getAmount() * row.getSelectedVatCode().getVatPercentage()/100);
                row.setVatAmount(row.getAmount() * row.getSelectedVatCode().getVatPercentage() / 100);
                row.setAmountAfterVAT(row.getAmount() + row.getVatAmount());

            } else {
                row.setVatAmount(0);
                row.setAmountAfterVAT(row.getAmount());

            }
        }else {
            row.setVatAmount(0);
            row.setAmountAfterVAT(row.getAmount());
        }
    }


    public static void recalculateItemsVAT(List<CheckItemsModel> lstCheckItems,
                                           VATCodeModel custVendVatCodeModel,
                                           CompSetupModel compSetupModel) {

        //compSetup= compSetupModel;
        for (CheckItemsModel item : lstCheckItems) {
            if (item.getSelectedItems() != null) {
                item.setSelectedVatCode(custVendVatCodeModel);
                item.setNotAllowEditVAT(true);
                getItemVatAmount(compSetupModel, item);
            }
        }
    }

    public static void recalculateCashInvoiceItemsVAT(List<CashInvoiceGridData> lstCashInvoiceCheckItem,
                                           VATCodeModel custVendVatCodeModel,
                                           CompSetupModel compSetupModel) {

        for (CashInvoiceGridData item : lstCashInvoiceCheckItem) {
            if (item.getSelectedItems() != null) {
                item.setSelectedVatCode(custVendVatCodeModel);
                item.setNotAllowEditVAT(true);
                getCashInvoiceItemVatAmount(compSetupModel, item);
            }
        }
    }

    public static void getCashInvoiceItemVatAmount(CompSetupModel compSetup,CashInvoiceGridData row) {
        if (compSetup.getUseVAT().equals("Y")) {
            if (row.getSelectedVatCode().getVatPercentage() > 0 && row.getSelectedVatCode().getVatType().equals("T")) {
                //Math.round(row.getAmount() * row.getSelectedVatCode().getVatPercentage()/100);
                row.setVatAmount(row.getInvoiceAmmount() * row.getSelectedVatCode().getVatPercentage() / 100);
                row.setUnitPriceWithVAT(row.getInvoiceRate() + (row.getInvoiceRate() * row.getSelectedVatCode().getVatPercentage() / 100));
                row.setAmountAfterVAT(row.getInvoiceAmmount() + row.getVatAmount());

            } else {
                row.setVatAmount(0);
                row.setUnitPriceWithVAT(row.getInvoiceRate());
                row.setAmountAfterVAT(row.getInvoiceAmmount());

            }
        }else {
            row.setVatAmount(0);
            row.setUnitPriceWithVAT(row.getInvoiceRate());
            row.setAmountAfterVAT(row.getInvoiceAmmount());
        }
    }



    public static void getItemVatAmount(CompSetupModel compSetup,CheckItemsModel row) {
        if (compSetup.getUseVAT().equals("Y")) {
            if (row.getSelectedVatCode().getVatPercentage() > 0 && row.getSelectedVatCode().getVatType().equals("T")) {
                //Math.round(row.getAmount() * row.getSelectedVatCode().getVatPercentage()/100);
                row.setVatAmount(row.getAmount() * row.getSelectedVatCode().getVatPercentage() / 100);
                row.setAmountAfterVAT(row.getAmount() + row.getVatAmount());

            } else {
                row.setVatAmount(0);
                row.setAmountAfterVAT(row.getAmount());

            }
        }else {
            row.setVatAmount(0);
            row.setAmountAfterVAT(row.getAmount());
        }
    }

    public static void selectItemsVAT(CheckItemsModel type,
                                           VATCodeModel custVendVatCodeModel,
                                           CompSetupModel compSetup,
                                      List<VATCodeModel> lstVatCodeList) {


        if(compSetup.getUseVAT().equals("Y")) {
            if (type.getSelectedItems() != null) {

                if(custVendVatCodeModel!=null)
                {
                    type.setSelectedVatCode(custVendVatCodeModel);
                    getItemVatAmount(compSetup,type);
                    //disable AllowEditing = False
                    type.setNotAllowEditVAT(true);
                    return;
                }

                if (type.getSelectedItems().getPurchaseVATKey() > 0) {
                    VATCodeModel vatCodeModel = lstVatCodeList.stream().filter(x -> x.getVatKey() == type.getSelectedItems().getPurchaseVATKey()).findFirst().orElse(null);
                    if (vatCodeModel != null) {
                        type.setSelectedVatCode(vatCodeModel);
                    } else {
                        type.setSelectedVatCode(lstVatCodeList.get(0));
                    }
                } else {
                    type.setSelectedVatCode(lstVatCodeList.get(0));
                }
                type.setNotAllowEditVAT(false);
                getItemVatAmount(compSetup,type);
            }
        }else {
            getItemVatAmount(compSetup,type);
        }

    }

    public static double getNewTotalAmount(List<ExpensesModel> lstExpenses , List<CheckItemsModel> lstCheckItems , List<CheckFAItemsModel> lstCheckFAItems) {
        double ExpAmount = 0;
        for (ExpensesModel item : lstExpenses) {
            ExpAmount += item.getAmountAfterVAT();
        }
        double toalCheckItemsAmount = 0;
        for (CheckItemsModel item : lstCheckItems) {
            toalCheckItemsAmount += item.getAmountAfterVAT();
        }
        double toalCheckFAItemsAmount = 0;
        for (CheckFAItemsModel item : lstCheckFAItems) {
            toalCheckFAItemsAmount += item.getAmount();
        }
        return   ExpAmount + toalCheckItemsAmount + toalCheckFAItemsAmount;
    }


    public static String setLabelExpenses(List<ExpensesModel> lstExpenses) {
        double ExpAmount = 0;
        for (ExpensesModel item : lstExpenses) {
            ExpAmount += item.getAmountAfterVAT();
        }

        return  "Expenses " + String.valueOf(ExpAmount);
    }

    public static String setLabelCheckItems(List<CheckItemsModel> lstCheckItems) {
        double toalCheckItemsAmount = 0;
        for (CheckItemsModel item : lstCheckItems) {
            toalCheckItemsAmount += item.getAmountAfterVAT();
        }
        return  "Items " + String.valueOf(toalCheckItemsAmount);
    }


    public static void selectCashInvoiceItemsVAT(CashInvoiceGridData type,
                                      VATCodeModel custVendVatCodeModel,
                                      CompSetupModel compSetup,
                                      List<VATCodeModel> lstVatCodeList) {


        if(compSetup.getUseVAT().equals("Y")) {
            if (type.getSelectedItems() != null) {

                if(custVendVatCodeModel!=null)
                {
                    type.setSelectedVatCode(custVendVatCodeModel);
                    getCashInvoiceItemVatAmount(compSetup,type);
                    //disable AllowEditing = False
                    type.setNotAllowEditVAT(true);
                    return;
                }

                if (type.getSelectedItems().getPurchaseVATKey() > 0) {
                    VATCodeModel vatCodeModel = lstVatCodeList.stream().filter(x -> x.getVatKey() == type.getSelectedItems().getPurchaseVATKey()).findFirst().orElse(null);
                    if (vatCodeModel != null) {
                        type.setSelectedVatCode(vatCodeModel);
                    } else {
                        type.setSelectedVatCode(lstVatCodeList.get(0));
                    }
                } else {
                    type.setSelectedVatCode(lstVatCodeList.get(0));
                }
                type.setNotAllowEditVAT(false);
                getCashInvoiceItemVatAmount(compSetup,type);
            }
        }else {
            getCashInvoiceItemVatAmount(compSetup,type);
        }

    }


    public static void recalculatePurchaseOrderVAT(List<PurchaseRequestGridData> lstCashInvoiceCheckItem,
                                                      VATCodeModel custVendVatCodeModel,
                                                      CompSetupModel compSetupModel) {

        for (PurchaseRequestGridData item : lstCashInvoiceCheckItem) {
            if (item.getSelectedItem() != null) {
                item.setSelectedVatCode(custVendVatCodeModel);
                item.setNotAllowEditVAT(true);
                getPurchaseOrderItemVatAmount(compSetupModel, item);
            }
        }
    }

    public static void getPurchaseOrderItemVatAmount(CompSetupModel compSetup,PurchaseRequestGridData row) {
        if (compSetup.getUseVAT().equals("Y")) {
            if (row.getSelectedVatCode().getVatPercentage() > 0 && row.getSelectedVatCode().getVatType().equals("T")) {
                //Math.round(row.getAmount() * row.getSelectedVatCode().getVatPercentage()/100);
                row.setVatAmount(row.getAmount() * row.getSelectedVatCode().getVatPercentage() / 100);
                row.setAmountAfterVAT(row.getAmount() + row.getVatAmount());

            } else {
                row.setVatAmount(0);
                row.setAmountAfterVAT(row.getAmount());

            }
        }else {
            row.setVatAmount(0);
            row.setAmountAfterVAT(row.getAmount());
        }
    }

    public static void selectPurchasrOrderItemsVAT(PurchaseRequestGridData type,
                                      VATCodeModel custVendVatCodeModel,
                                      CompSetupModel compSetup,
                                      List<VATCodeModel> lstVatCodeList) {


        if(compSetup.getUseVAT().equals("Y")) {
            if (type.getSelectedItem() != null) {

                if(custVendVatCodeModel!=null)
                {
                    type.setSelectedVatCode(custVendVatCodeModel);
                    getPurchaseOrderItemVatAmount(compSetup,type);
                    //disable AllowEditing = False
                    type.setNotAllowEditVAT(true);
                    return;
                }

                if (type.getSelectedItem().getPurchaseVATKey() > 0) {
                    VATCodeModel vatCodeModel = lstVatCodeList.stream().filter(x -> x.getVatKey() == type.getSelectedItem().getPurchaseVATKey()).findFirst().orElse(null);
                    if (vatCodeModel != null) {
                        type.setSelectedVatCode(vatCodeModel);
                    } else {
                        type.setSelectedVatCode(lstVatCodeList.get(0));
                    }
                } else {
                    type.setSelectedVatCode(lstVatCodeList.get(0));
                }
                type.setNotAllowEditVAT(false);
                getPurchaseOrderItemVatAmount(compSetup,type);
            }
        }else {
            getPurchaseOrderItemVatAmount(compSetup,type);
        }

    }

}

