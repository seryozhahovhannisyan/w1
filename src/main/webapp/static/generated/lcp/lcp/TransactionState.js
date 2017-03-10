/* Compiled with love by Gruntfile.js */
var TransactionState = [
 EXCHANGE_BALANCE = {    "id": 17,   "state":  "approved",   "description":  "when somebody starts transaction for pass to money"  },
 PENDING = {    "id": 15,   "state":  "pending transaction",   "description":  "when somebody starts transaction for pass to money"  },
 PASSED_TO_CHARGE = {    "id": 16,   "state":  "passed to charge",   "description":  "when somebody starts transaction for pass to money"  },
 ////////////// NEW = {    "id": ////////////// NEW  },
 SEND_MONEY = {    "id": 1,   "state":  "sendmoney money",   "description":  "when somebody starts transaction for pass to money"  },
 REQUEST_TRANSACTION = {    "id": 2,   "state":  "request transaction",   "description":  "when somebody starts transaction for ask to money"  },
 APPROVED = {    "id": 3,   "state":  "approved",   "description":  "when transaction is successfully done and approved"  },
 CANCEL = {    "id": 4,   "state":  "cancel",   "description":  "when transaction canceled by user"  },
 REJECTED = {    "id": 5,   "state":  "rejected transaction",   "description":  "when transaction was not approved"  },
 REJECTED_BY_ADMIN = {    "id": 6,   "state":  "rejected by admin",   "description":  "when a transaction rejected by admin "  },
 PURCHASE_FREEZE = {    "id": 7,   "state":  "Purchase freeze transaction",   "description":  "Purchase transaction freeze"  },
 PURCHASE_CANCEL = {    "id": 8,   "state":  "Purchase cancel transaction",   "description":  "Purchase transaction cancel"  },
 PURCHASE_CHARGE = {    "id": 9,   "state":  "Purchase charge transaction",   "description":  "Purchase transaction charge"  },
 MERCHANT_START = {    "id": 20,   "state":  "Merchant start transaction",   "description":  "Merchant start transaction"  },
 MERCHANT_TIMEOUT = {    "id": 21,   "state":  "Merchant timeout transaction",   "description":  "Merchant timeout transaction"  },
 MERCHANT_CANCEL = {    "id": 22,   "state":  "Merchant cancel transaction",   "description":  "Merchant cancel transaction"  },
 MERCHANT_APPROVE = {    "id": 23,   "state":  "Merchant approve transaction",   "description":  "Merchant approve transaction"  },
 CHARGE_AMOUNT = {    "id": 10,   "state":  "charge_amount",   "description":  "when transaction is successfully done and transferring was completed walletSetup receiving_amount will minused and balance plussed immediately"  },
 FUTURE_PAYMENT = {    "id": 11,   "state":  "future payment",   "description":  "when driver get cash money we will keep future payment that he will pay thiers administrator"  }
]
