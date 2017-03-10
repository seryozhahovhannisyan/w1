package com.connectto.wallet.web.action.wallet.load;

import com.connectto.general.dataaccess.service.IUserManager;
import com.connectto.general.model.Partition;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.User;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Initializer;
import com.connectto.general.util.ShellAction;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.dataaccess.service.general.IWalletLoggerManager;
import com.connectto.wallet.model.wallet.Wallet;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 9/3/14.
 */
public class SearchAction extends ShellAction {

    private static final Logger logger = Logger.getLogger(SearchAction.class.getSimpleName());
    private IWalletLoggerManager walletLoggerManager;

    public void setWalletLoggerManager(IWalletLoggerManager walletLoggerManager) {
        this.walletLoggerManager = walletLoggerManager;
    }

    private IUserManager userManager;
    private IWalletManager walletManager;

    private ResponseDto responseDto;
    private String searchLike;
    private List<User> searchUsers;

    private Integer currentPage;
    private Integer isLast = 0;

    private String orderType;


    public String searchUsers() {
        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_PARTITION);

        if (user == null || wallet == null) {
            logger.error("session time out");
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }
        if (Utils.isEmpty(orderType) || !Utils.isValidOrderType(orderType)) {
             orderType = "asc";
        }

        int paginationCount = Initializer.getPaginationCount();

        Map<String, Object> params = new HashMap<String, Object>();
        if(!Utils.isEmpty(searchLike)){
            searchLike = searchLike.trim().toLowerCase();
            String[] nameSurname = searchLike.split(" ");
            if(nameSurname != null && nameSurname.length == 2){
                params.put("searchLikeName", nameSurname[0].trim());
                params.put("searchLikeSurname", nameSurname[1].trim());
            } else {
                params.put("searchLike", searchLike);
            }
        }

        params.put("partitionId", partition.getId());
        try {
            if (currentPage < 2) {
                currentPage = 1;
            }

            params.put("limit", paginationCount);
            params.put("offset", (currentPage - 1) * paginationCount);
            params.put("exceptUserId", user.getId());
            params.put("orderType_" + orderType, orderType);

            List<Long> userIdes = walletManager.getBlockedUserIdes(user.getId());
            if(!Utils.isEmpty(userIdes)){
                params.put("exceptUserIdes", userIdes);
            }

            searchUsers = userManager.getByParams(params);
            if (Utils.isEmpty(searchUsers) || searchUsers.size() < paginationCount) {
                isLast = 1;
            } else {
                currentPage++;
            }
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }
        return SUCCESS;
    }

    public String searchBlockedUsers() {
        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_PARTITION);

        if (user == null || wallet == null) {
            logger.error("session time out");
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }
        if (Utils.isEmpty(orderType) || !Utils.isValidOrderType(orderType)) {
             orderType = "asc";
        }

        int paginationCount = Initializer.getPaginationCount();

        Map<String, Object> params = new HashMap<String, Object>();
        if(!Utils.isEmpty(searchLike)){
            searchLike = searchLike.trim().toLowerCase();
            String[] nameSurname = searchLike.split(" ");
            if(nameSurname != null && nameSurname.length == 2){
                params.put("searchLikeName", nameSurname[0].trim());
                params.put("searchLikeSurname", nameSurname[1].trim());
            } else {
                params.put("searchLike", searchLike);
            }
        }

        params.put("partitionId", partition.getId());
        try {
            if (currentPage < 2) {
                currentPage = 1;
            }

            params.put("limit", paginationCount);
            params.put("offset", (currentPage - 1) * paginationCount);
            params.put("exceptUserId", user.getId());
            params.put("orderType_" + orderType, orderType);

            List<Long> userIdes = walletManager.getBlockedUserIdes(user.getId());
            if (Utils.isEmpty(userIdes) ) {
                isLast = 1;
                responseDto.setStatus(ResponseStatus.SUCCESS);
                return SUCCESS;
            }
            params.put("userIdes", userIdes);

            searchUsers = userManager.getByParams(params);

            if (Utils.isEmpty(searchUsers) || searchUsers.size() < paginationCount) {
                isLast = 1;
            } else {
                currentPage++;
            }
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }
        return SUCCESS;
    }

    /*
     *##################################################################################################################
     * Getters
     *##################################################################################################################
     */

    public String getSearchLike() {
        return searchLike;
    }

    public List<User> getSearchUsers() {
        return searchUsers;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public Integer getIsLast() {
        return isLast;
    }

    public ResponseDto getResponseDto() {
        return responseDto;
    }



    /*
     *##################################################################################################################
     * Setters
     *##################################################################################################################
     */

    public void setSearchLike(String searchLike) {
        this.searchLike = searchLike;
    }

    public void setCurrentPage(String currentPage) {
        try {
            this.currentPage = Integer.parseInt(currentPage);
        } catch (Exception e) {
            this.currentPage = -1;
            logger.error(String.format("Incorrect current Page ,  page[%s]=", currentPage));
        }

    }
/*

    public void setSelectedCurrencyTypeId(int selectedCurrencyTypeId) {
        this.selectedCurrencyTypeId = selectedCurrencyTypeId;
    }
*/

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public void setWalletManager(IWalletManager walletManager) {
        this.walletManager = walletManager;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }
}
