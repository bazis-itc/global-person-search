SELECT 
  [surname] = surname.A_NAME,
  [name] = firstName.A_NAME,
  [patronymic] = patronymic.A_NAME,
  [birthdate] = person.BIRTHDATE,
  [address] = person.A_REGFLAT,
  [boroughId] = person.A_SERV,
  [localId] = person.A_LOCAL_OUID,
  [snils] = person.A_SNILS,
  [boroughName] = borough.A_RAION_NAME,
  [passport] = (
    SELECT TOP 1 
      ISNULL(passport.DOCUMENTSERIES, '') + ' ' 
      + ISNULL(passport.DOCUMENTSNUMBER, '')
    FROM IDEN_DOC_REF_REGISTRY passport
      JOIN SPR_DOC_STATUS docStatus 
        ON docStatus.A_OUID = passport.A_DOCSTATUS
        AND docStatus.A_CODE = 'active'
    WHERE passport.A_LD = person.A_OUID
      AND ISNULL(passport.A_STATUS, 10) = 10
    ORDER BY 
      passport.ISSUEEXTENSIONSDATE DESC,
      passport.A_CREATEDATE DESC,
      passport.A_OUID DESC
  ),
  [status] = personStatus.A_NAME,
  [regOffDate] = regOff.offDate,
  [regOffReason] = regOff.reason
FROM REGISTER_PERSONAL_CARD person
  LEFT JOIN SPR_FIO_SURNAME surname ON surname.OUID = person.SURNAME
  LEFT JOIN SPR_FIO_NAME firstName ON firstName.OUID = person.A_NAME
  LEFT JOIN SPR_FIO_SECONDNAME patronymic ON patronymic.OUID = person.A_SECONDNAME
  LEFT JOIN REFERENCE_INF borough ON borough.A_OUID = person.A_SERV
  LEFT JOIN SPR_PC_STATUS personStatus ON personStatus.OUID = person.A_PCSTATUS
  OUTER APPLY (
    SELECT TOP(1) 
      offDate = regOff.A_DATE,
      reason = reason.A_NAME
    FROM LINK_LD_REASON_REGISTRY link
      JOIN REASON_REF_REGISTRY regOff ON regOff.A_OUID = link.A_TOID
        AND ISNULL(regOff.A_STATUS, 10) = 10
      LEFT JOIN SPR_RES_REMUV reason ON reason.OUID = regOff.A_NAME
    WHERE link.A_FROMID = person.A_OUID
    ORDER BY regOff.A_DATE
  ) AS regOff
WHERE 
  surname.A_NAME + ' ' + firstName.A_NAME + ' ' + ISNULL(patronymic.A_NAME, '') = ?
  AND DATEDIFF(DAY, person.BIRTHDATE, CONVERT(DATETIME, ?, 120)) = 0
  OR REPLACE(REPLACE(person.A_SNILS, ' ', ''), '-', '') 
    = REPLACE(REPLACE(?, ' ', ''), '-', '')