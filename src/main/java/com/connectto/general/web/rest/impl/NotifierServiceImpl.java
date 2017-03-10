package com.connectto.general.web.rest.impl;

import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.web.rest.INotifierService;
import com.connectto.general.web.rest.util.PathConstant;
import com.connectto.wallet.dataaccess.service.ITransactionManager;
import com.connectto.wallet.model.wallet.TransactionNotifier;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import org.apache.log4j.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Serozh
 * Date: 05.05.13
 * Time: 20:30
 * To change this template use File | Settings | File Templates.
 */
@Path(PathConstant.NOTIFIER)
@Produces(MediaType.APPLICATION_JSON)
public class NotifierServiceImpl implements INotifierService {

    private static final Logger logger = Logger.getLogger(NotifierServiceImpl.class.getSimpleName());
    private ITransactionManager transactionManager;

    public void setTransactionManager(ITransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    @POST
    @Path(PathConstant.LOAD_TRANSACTION_NOTIFIER)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseDto loadTransactionNotifier(@PathParam("id") String id) {

        ResponseDto responseDto = new ResponseDto();

        Date currentDate = new Date(System.currentTimeMillis());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sendMoneyId", TransactionState.SEND_MONEY.getId());
        params.put("requestTransactionId", TransactionState.REQUEST_TRANSACTION.getId());
        params.put("strictStates", new Integer[]{TransactionState.PENDING.getId()});
        //must be exists
        params.put("currentDate", currentDate);
        params.put("walletId", id);

        try {
            TransactionNotifier transactionNotifier = transactionManager.getTransactionNotifier(params);
            responseDto.setStatus(ResponseStatus.SUCCESS);
            //responseDto.setData(transactionNotifier);
            responseDto.addResponse("data", transactionNotifier);
        } catch (InternalErrorException e) {
            logger.error(e);
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return responseDto;
    }
}
