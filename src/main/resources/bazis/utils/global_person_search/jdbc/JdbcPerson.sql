SELECT
  [type] = 'appoint',
  [mspGuid] = msp.GUID,
  [mspName] = msp.A_NAME,
  [category] = category.A_NAME,
  [status] = appointStatus.A_NAME,
  [childSurname] = surname.A_NAME,
  [childName] = firstName.A_NAME,
  [childPatronymic] = patronymic.A_NAME,
  [periods] = (
    SELECT 
      ISNULL(CONVERT(VARCHAR(10), term.STARTDATE, 120), 'NULL') + ' '
      + ISNULL(CONVERT(VARCHAR(10), term.A_LASTDATE, 120), 'NULL') + '|'
    FROM SPR_SERV_PERIOD term
    WHERE term.A_SERV = appoint.OUID
      AND ISNULL(term.A_STATUS, 10) = 10
    FOR XML PATH ('')
  ),
  [payments] = (
    SELECT 
      CONVERT(VARCHAR(10), pay.PAIDDATE, 120) +  ' '
      + CAST(pay.A_YEAR AS VARCHAR) + ' '
      + CAST(payMonth.A_CODE AS VARCHAR) + ' '
      + CAST(pay.AMOUNT AS VARCHAR) + '|'
    FROM WM_PAY_CALC accrual
      JOIN WM_PAIDAMOUNTS pay ON pay.A_PAYCALC = accrual.OUID
        AND ISNULL(pay.A_STATUS, 10) = 10
      JOIN SPR_STATUS_PAYMENT payStatus 
        ON payStatus.A_ID = pay.A_STATUSPRIVELEGE
      JOIN SPR_MONTH payMonth ON payMonth.A_ID = pay.A_MONTH
    WHERE accrual.A_MSP = appoint.OUID
      AND payStatus.A_CODE = 10 --Выплачено (закрыто)
      AND ISNULL(accrual.A_STATUS, 10) = 10
    FOR XML PATH ('')
  ),
  [regDate] = NULL,
  [appointDate] = NULL
FROM ESRN_SERV_SERV appoint
  LEFT JOIN SPR_NPD_MSP_CAT basement 
    ON basement.A_ID = appoint.A_SERV
  LEFT JOIN PPR_SERV msp ON basement.A_MSP = msp.A_ID
  LEFT JOIN PPR_CAT category ON category.A_ID = basement.A_CATEGORY
  LEFT JOIN WM_PERSONAL_CARD child 
    LEFT JOIN SPR_FIO_SURNAME surname ON surname.OUID = child.SURNAME
    LEFT JOIN SPR_FIO_NAME firstName ON firstName.OUID = child.A_NAME
    LEFT JOIN SPR_FIO_SECONDNAME patronymic ON patronymic.OUID = child.A_SECONDNAME
  ON child.OUID = appoint.A_CHILD
  LEFT JOIN SPR_STATUS_PROCESS appointStatus 
    ON appointStatus.A_ID = appoint.A_STATUSPRIVELEGE
WHERE ISNULL(appoint.A_STATUS, 10) = 10
  AND appoint.A_PERSONOUID = @id
UNION
SELECT
  [type] = 'petition',
  [mspGuid] = msp.GUID,
  [mspName] = msp.A_NAME,
  [category] = category.A_NAME,
  [status] = petitionStatus.A_NAME,
  [childSurname] = NULL,
  [childName] = NULL,
  [childPatronymic] = NULL,
  [periods] = NULL,
  [payments] = NULL,
  [regDate] = appeal.A_DATE_REG,
  [appointDate] = petition.A_DONEDATE
FROM WM_PETITION petition
  JOIN WM_APPEAL_NEW appeal ON appeal.OUID = petition.OUID
  JOIN PPR_SERV msp ON msp.A_ID = petition.A_MSP
  LEFT JOIN PPR_CAT category ON category.A_ID = petition.A_CATEGORY
  LEFT JOIN SPR_STATUS_PROCESS petitionStatus 
    ON petitionStatus.A_ID = petition.A_STATUSPRIVELEGE 
WHERE ISNULL(petition.A_STATUS, 10) = 10
  AND petition.A_MSPHOLDER = @id
    