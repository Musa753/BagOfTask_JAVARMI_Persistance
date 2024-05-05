-- Création de la table Client
CREATE TABLE Client (
    ClientID NUMBER PRIMARY KEY,
    Nom VARCHAR2(50),
    Prenom VARCHAR2(50),
    Adresse VARCHAR2(100),
    NumeroTelephone VARCHAR2(20)
);

-- Création de la table Compte
CREATE TABLE Compte (
    CompteID NUMBER PRIMARY KEY,
    ClientID NUMBER,
    Solde NUMBER,
    TypeDeCompte VARCHAR2(20),
    CONSTRAINT fk_ClientID FOREIGN KEY (ClientID) REFERENCES Client(ClientID)
);

-- Création de la table Transaction
CREATE TABLE Transaction (
    TransactionID NUMBER PRIMARY KEY,
    CompteID NUMBER,
    TypeDeTransaction VARCHAR2(20),
    Montant NUMBER,
    DateTransaction DATE,
    CONSTRAINT fk_CompteID FOREIGN KEY (CompteID) REFERENCES Compte(CompteID)
);
CREATE OR REPLACE PROCEDURE RemplirBaseDeDonnees AS
    v_nom VARCHAR2(50);
    v_prenom VARCHAR2(50);
    v_numero_tel VARCHAR2(20);
    v_adresse VARCHAR2(1000);
    v_numero_bat NUMBER;

    -- Declare arrays for names and street names
    Noms DBMS_SQL.VARCHAR2A;
    Prenoms DBMS_SQL.VARCHAR2A;
    NomsRue DBMS_SQL.VARCHAR2A;
BEGIN
    -- Initialize names and street names
    Noms(1) := 'TRAORE';
    Noms(2) := 'NIBAREKE';
    Noms(3) := 'GILLET';
    Noms(4) := 'DIARRA';
    Noms(5) := 'BELLEGUEULLE';
    Noms(6) := 'Michaud';
    Noms(7) := 'BAGAYOKO';
    Noms(8) := 'CISSE';
    Noms(9) := 'SANOGO';
    Noms(10) := 'DEMBELE';

    Prenoms(1) := 'Moussa';
    Prenoms(2) := 'Thérence';
    Prenoms(3) := 'Ibrahim';
    Prenoms(4) := 'Christophe';
    Prenoms(5) := 'Mathieu';
    Prenoms(6) := 'Emilien';
    Prenoms(7) := 'Adama';
    Prenoms(8) := 'Alima';
    Prenoms(9) := 'Ousmane';
    Prenoms(10) := 'Mohamed';

    NomsRue(1) := 'Boulevard Mansart';
    NomsRue(2) := 'Avenue Alain Savary';
    NomsRue(3) := 'Rue des Rossoirs';
    NomsRue(4) := 'Republique';
    NomsRue(5) := 'Rue Nelson Mandela';

    FOR i IN 1..100 LOOP
        -- Génération aléatoire d'un nom et d'un prénom
        v_nom := Noms(TRUNC(DBMS_RANDOM.VALUE(1, 10)));
        v_prenom := Prenoms(TRUNC(DBMS_RANDOM.VALUE(1, 10)));

        -- Génération aléatoire de numéros de téléphone
        v_numero_tel := TO_CHAR(TRUNC(DBMS_RANDOM.VALUE(1000000000, 9999999999)));

        -- Génération aléatoire d'une adresse en effectuant des permutations
        v_adresse :=NomsRue(DBMS_RANDOM.VALUE(1, 5)) || ',' || TO_CHAR(TRUNC(DBMS_RANDOM.VALUE(1, 100)));
         INSERT INTO Client(ClientID,Nom,Prenom,Adresse,NumeroTelephone)
         VALUES(i,v_nom,v_prenom,v_adresse,v_numero_tel);
        -- Chaque client a au moins un compte
        INSERT INTO Compte(CompteID, ClientID, Solde, TypeDeCompte)
        VALUES (i, i, DBMS_RANDOM.VALUE(1000, 10000), 'Courant');

        -- Générez quelques transactions pour chaque compte
        FOR j IN 1..DBMS_RANDOM.VALUE(1, 5) LOOP
            INSERT INTO Transaction(TransactionID, CompteID, TypeDeTransaction, Montant, DateTransaction)
            VALUES (i * 100 + j, i, CASE WHEN DBMS_RANDOM.VALUE < 0.5 THEN 'Depot' ELSE 'Retrait' END, DBMS_RANDOM.VALUE(10, 500), SYSDATE - DBMS_RANDOM.VALUE(1, 365));
        END LOOP;

    END LOOP;

    COMMIT;
END  RemplirBaseDeDonnees;
/
CREATE OR REPLACE PROCEDURE RemplirBaseDeDonnees AS
    v_nom VARCHAR2(50);
    v_prenom VARCHAR2(50);
    v_numero_tel VARCHAR2(20);
    v_adresse VARCHAR2(100);
    v_numero_bat NUMBER;

    -- Declare arrays for names and street names
    Noms DBMS_SQL.VARCHAR2A;
    Prenoms DBMS_SQL.VARCHAR2A;
    NomsRue DBMS_SQL.VARCHAR2A;
BEGIN
    -- Initialize names and street names
    Noms(1) := 'TRAORE';
    Noms(2) := 'NIBAREKE';
    Noms(3) := 'GILLET';
    Noms(4) := 'DIARRA';
    Noms(5) := 'BELLEGUEULLE';
    Noms(6) := 'Michaud';
    Noms(7) := 'BAGAYOKO';
    Noms(8) := 'CISSE';
    Noms(9) := 'SANOGO';
    Noms(10) := 'DEMBELE';

    Prenoms(1) := 'Moussa';
    Prenoms(2) := 'Thérence';
    Prenoms(3) := 'Ibrahim';
    Prenoms(4) := 'Christophe';
    Prenoms(5) := 'Mathieu';
    Prenoms(6) := 'Emilien';
    Prenoms(7) := 'Adama';
    Prenoms(8) := 'Alima';
    Prenoms(9) := 'Ousmane';
    Prenoms(10) := 'Mohamed';

    NomsRue(1) := 'Boulevard Mansart';
    NomsRue(2) := 'Avenue Alain Savary';
    NomsRue(3) := 'Rue des Rossoirs';
    NomsRue(4) := 'Republique';
    NomsRue(5) := 'Rue Nelson Mandela';

    FOR i IN 1..100 LOOP
        -- Génération aléatoire d'un nom et d'un prénom
        v_nom := Noms(TRUNC(DBMS_RANDOM.VALUE(1, 10)));
        v_prenom := Prenoms(TRUNC(DBMS_RANDOM.VALUE(1, 10)));

        -- Génération aléatoire de numéros de téléphone
        v_numero_tel := TO_CHAR(TRUNC(DBMS_RANDOM.VALUE(1000000000, 9999999999)));

        -- Génération aléatoire d'une adresse en effectuant des permutations
        v_adresse := NomsRue(TRUNC(DBMS_RANDOM.VALUE(1, 5)) || ', ' || TO_CHAR(TRUNC(DBMS_RANDOM.VALUE(1, 100))));

        -- Rest of your code
        -- ...

    END LOOP;

    COMMIT;
END RemplirBaseDeDonnees;
-- Créez un déclencheur BEFORE UPDATE sur la table Compte
CREATE OR REPLACE TRIGGER VerifierSoldeAvantRetrait
BEFORE UPDATE ON Compte
FOR EACH ROW
DECLARE
    SoldeCompte NUMBER;
BEGIN
    -- Récupérez le solde actuel du compte
    SELECT Solde INTO SoldeCompte
    FROM Compte
    WHERE CompteID = :NEW.CompteID;

    -- Vérifiez si la nouvelle valeur du solde est inférieure au solde actuel
    IF :NEW.Solde < SoldeCompte THEN
    -- Empêchez la mise à jour du solde en générant une erreur
    RAISE_APPLICATION_ERROR(-20002, 'Solde insuffisant pour effectuer le retrait.');
    END IF;
END;
/


-- Créez un déclencheur AFTER UPDATE sur la table Compte
CREATE OR REPLACE TRIGGER EnregistrerModificationSolde
AFTER UPDATE ON Compte
FOR EACH ROW
DECLARE
    MontantTransaction NUMBER;
    TypeTransaction VARCHAR2(20);
BEGIN
    -- Calculez le montant de la transaction
    MontantTransaction := :NEW.Solde - :OLD.Solde;

    -- Déterminez le type de transaction en fonction du montant
    IF MontantTransaction > 0 THEN
        TypeTransaction := 'Depot';
    ELSIF MontantTransaction < 0 THEN
        TypeTransaction := 'Retrait';
    ELSE
        -- Aucune modification de solde, pas de transaction
        RETURN;
    END IF;

    -- Enregistrez la transaction
    INSERT INTO Transaction (TransactionID, CompteID, TypeDeTransaction, Montant, DateTransaction)
    VALUES (SequenceTransaction.NEXTVAL, :OLD.CompteID, TypeTransaction, ABS(MontantTransaction), SYSDATE);
END;
/
