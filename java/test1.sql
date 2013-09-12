--
-- 2013-09-05_02.sql
--
-- PA-16232: Edit Western & Southern Portal content inject text
--
--
-- DML script
--
-- Execution plan: PRE-DEPLOYMENT
--
-- Daniel Yu
--

DECLARE @oldFragmentId_WesternSouthernText INT = (SELECT TOP 1 Custom_Html_Fragment_Id FROM Custom_Html_Fragments WHERE Description = N'Instructions above the portal for W&S')
DECLARE @userId_dyu INT = 1737578

IF (@oldFragmentId_WesternSouthernText IS NOT NULL)
BEGIN
	UPDATE Custom_Html_Fragment_Languages
	SET Fragment_Html = N'<font style="color:#000000; font-size:15px;"><strong>IMPORTANT INFORMATION BELOW: PLEASE READ.</strong><br/><br/>Welcome to the second portion of our employment application/assessment.   In order to get started,  you will need to enter your zip code below and select the location you are applying for again. Please be careful to select the same location that you selected at the beginning of the employment application. <strong>You will need to create a new login and password for this portion of the assessment as the login information you created for the application does not carry over.</strong> You will create your login information on the following registration page after you enter your zip code and choose the location you are applying for.<br/><br/>If you have previously began taking or have completed this upcoming assessment portion of the application in the past, please enter your PeopleAnswers login and password at the bottom of the page. If you have any questions, please contact PeopleAnswers support at support@peopleanswers.com.<br/><br/>Thank you for your interest in a position with Western & Southern Life. For all positions within Western & Southern Life, we have a two-step process that should take you approximately 25-30 minutes in total. We do encourage you to complete the application process in one session when possible. In the event you do not have this amount of time to complete the application process, you will have the opportunity to log in and out. Thank you again for your interest in Western & Southern Life.<br/><br/></font>',
		Last_Updated_By = @userId_dyu,
		Last_Updated_Date = SYSDATETIME()
	WHERE Custom_Html_Fragment_Id = @oldFragmentId_WesternSouthernText
END


EXEC addDBUpdate N'2013-09-05_02'
GO

--------------
-- ROLLBACK --
--------------
/*
*DECLARE @oldFragmentId_WesternSouthernText INT = (SELECT TOP 1 Custom_Html_Fragment_Id FROM Custom_Html_Fragments WHERE Description = N'Instructions above the portal for W&S')
*DECLARE @userId_dyu INT = 1737578
*IF (@oldFragmentId_WesternSouthernText IS NOT NULL)
*BEGIN
*	UPDATE Custom_Html_Fragment_Languages
*	SET Fragment_Html = N'<font style="color:#000000; font-size:15px;">Welcome to the second portion of our employment application/assessment.   In order to get started,  you will need to enter your zip code below and select the location you are applying for again. Please be careful to select the same location that you selected at the beginning of the employment application. You will need to create a new login and password for this portion of the assessment as the login information you created for the application does not carry over. You will create your login information on the following registration page after you enter your zip code and choose the location you are applying for.<br/><br/>If you have previously began taking or have completed this upcoming assessment portion of the application in the past, please enter your PeopleAnswers login and password at the bottom of the page. If you have any questions, please contact PeopleAnswers support at support@peopleanswers.com.<br/><br/>Thank you for your interest in a position with Western & Southern Life. For all positions within Western & Southern Life, we have a two-step process that should take you approximately 25-30 minutes in total. We do encourage you to complete the application process in one session when possible. In the event you do not have this amount of time to complete the application process, you will have the opportunity to log in and out. Thank you again for your interest in Western & Southern Life.<br/><br/></font>',
*		Last_Updated_By = @userId_dyu,
*		Last_Updated_Date = SYSDATETIME()
*	WHERE Custom_Html_Fragment_Id = @oldFragmentId_WesternSouthernText
*END
*
*EXEC addDBUpdate N'2013-09-05_02_Rollback'
*GO
*/