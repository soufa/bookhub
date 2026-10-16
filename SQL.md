# SQL - Questions d'Interview

Nombre de questions : 100
Généré le : 2026-10-16 16:29:29

Table des matières
Questions de Base

Requêtes et Jointures

Index et Optimisation

Transactions et Concurrence

Administration

Questions Pratiques

Bonnes Pratiques

1. Questions de Base
1. Qu'est-ce que SQL ?
Réponse :
SQL (Structured Query Language) est un langage standardisé pour gérer et manipuler les bases de données relationnelles.

Types de commandes :

DDL : CREATE, ALTER, DROP

DML : SELECT, INSERT, UPDATE, DELETE

DCL : GRANT, REVOKE

TCL : COMMIT, ROLLBACK, SAVEPOINT

2. Qu'est-ce qu'une clé primaire (Primary Key) ?
Réponse :
Identifiant unique pour chaque enregistrement d'une table.

sql
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY,
    Name VARCHAR(50),
    Department VARCHAR(50)
);
Caractéristiques : Unique, Non null, Une seule par table

3. Qu'est-ce qu'une clé étrangère (Foreign Key) ?
Réponse :
Champ qui référence la clé primaire d'une autre table, assurant l'intégrité référentielle.

sql
CREATE TABLE Orders (
    OrderID INT PRIMARY KEY,
    OrderDate DATE,
    CustomerID INT,
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
);
4. Qu'est-ce qu'une clé unique (Unique Key) ?
Réponse :
Assure que toutes les valeurs d'une colonne sont uniques.

sql
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY,
    Email VARCHAR(100) UNIQUE
);
Différence avec Primary Key :

Unique peut contenir NULL (une seule fois)

Plusieurs Unique Keys par table

5. Qu'est-ce qu'une clé composite ?
Réponse :
Clé primaire composée de plusieurs colonnes.

sql
CREATE TABLE Enrollment (
    StudentID INT,
    CourseID INT,
    EnrollmentDate DATE,
    PRIMARY KEY (StudentID, CourseID)
);
6. Qu'est-ce qu'une clé candidate ?
Réponse :
Colonne ou ensemble de colonnes pouvant identifier uniquement un enregistrement. Une table peut avoir plusieurs clés candidates, dont une devient la clé primaire.

7. Qu'est-ce qu'une contrainte ?
Réponse :
Règle appliquée aux colonnes pour assurer l'intégrité des données.

Types : PRIMARY KEY, FOREIGN KEY, UNIQUE, CHECK, DEFAULT, NOT NULL

8. Qu'est-ce qu'une contrainte CHECK ?
Réponse :
Valide une condition sur les valeurs d'une colonne.

sql
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY,
    Name VARCHAR(50),
    Age INT CHECK (Age >= 18),
    Salary DECIMAL(10,2) CHECK (Salary > 0)
);
9. Qu'est-ce qu'une contrainte DEFAULT ?
Réponse :
Valeur par défaut si aucune valeur n'est fournie.

sql
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY,
    Name VARCHAR(50),
    Status VARCHAR(20) DEFAULT 'Active',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
10. Qu'est-ce qu'un index ?
Réponse :
Structure de données qui accélère les recherches.

Types :

Clustered Index : Tri physique des données (1 par table)

Non-Clustered Index : Pointeur vers les données

sql
CREATE INDEX idx_employee_name ON Employees(Name);
CREATE INDEX idx_dept_name ON Employees(DepartmentID, Name);
11. Qu'est-ce qu'une vue (View) ?
Réponse :
Table virtuelle basée sur le résultat d'une requête.

sql
CREATE VIEW EmployeeView AS
SELECT e.Name, d.DepartmentName
FROM Employees e
JOIN Departments d ON e.DepartmentID = d.DepartmentID;

SELECT * FROM EmployeeView;
12. Qu'est-ce qu'une sous-requête (Subquery) ?
Réponse :
Requête imbriquée dans une autre requête.

sql
-- Sous-requête non corrélée
SELECT Name FROM Employees
WHERE DepartmentID = (
    SELECT DepartmentID FROM Departments WHERE DepartmentName = 'Sales'
);

-- Sous-requête corrélée
SELECT E1.Name FROM Employees E1
WHERE E1.Salary > (
    SELECT AVG(Salary) FROM Employees E2 
    WHERE E1.DepartmentID = E2.DepartmentID
);
13. Qu'est-ce qu'une procédure stockée (Stored Procedure) ?
Réponse :
Code SQL précompilé et réutilisable.

sql
CREATE PROCEDURE GetEmployeeDetails
    @DepartmentID INT
AS
BEGIN
    SELECT * FROM Employees WHERE DepartmentID = @DepartmentID;
END;

EXEC GetEmployeeDetails @DepartmentID = 1;
14. Qu'est-ce qu'un trigger ?
Réponse :
Code SQL qui s'exécute automatiquement sur un événement.

sql
CREATE TRIGGER trgAfterInsert ON Employees
AFTER INSERT
AS
BEGIN
    INSERT INTO AuditLog (Action, TableName, Timestamp)
    VALUES ('INSERT', 'Employees', GETDATE());
END;
15. Qu'est-ce que la normalisation ?
Réponse :
Organisation des données pour réduire la redondance.

Formes normales :

1NF : Pas de groupes répétitifs, valeurs atomiques

2NF : 1NF + dépendance fonctionnelle complète

3NF : 2NF + pas de dépendances transitives

BCNF : 3NF + toutes les dépendances sont sur des super-clés

16. Qu'est-ce que la dénormalisation ?
Réponse :
Combiner des tables normalisées pour améliorer les performances de lecture.

Quand l'utiliser : Requêtes de lecture fréquentes, Jointures coûteuses, Rapport et analyses

17. Qu'est-ce qu'une transaction ?
Réponse :
Séquence d'opérations traitées comme une unité atomique.

Propriétés ACID :

Atomicity : Tout ou rien

Consistency : État cohérent

Isolation : Indépendance des transactions

Durability : Persistance des données

sql
BEGIN TRANSACTION;
    UPDATE Accounts SET Balance = Balance - 100 WHERE AccountID = 1;
    UPDATE Accounts SET Balance = Balance + 100 WHERE AccountID = 2;
COMMIT;
18. Quelles sont les propriétés ACID ?
Réponse :

Atomicity : La transaction est indivisible

Consistency : La base reste cohérente

Isolation : Les transactions sont isolées

Durability : Les changements sont permanents

19. Qu'est-ce qu'un deadlock ?
Réponse :
Situation où deux transactions s'attendent mutuellement.

Exemple : Transaction 1 lock A et attend B ; Transaction 2 lock B et attend A

Prévention : Accéder aux ressources dans le même ordre, Timeouts, Détection automatique

20. Quelle est la différence entre DELETE et TRUNCATE ?
Réponse :

DELETE	TRUNCATE
Supprime ligne par ligne	Supprime tout
Peut avoir WHERE	Pas de WHERE
Loggé	Non loggé (plus rapide)
Rollback possible	Rollback difficile
Trigger exécuté	Trigger non exécuté
21. Quelle est la différence entre DROP et TRUNCATE ?
Réponse :

DROP : Supprime la table et sa structure

TRUNCATE : Supprime les données, garde la structure

22. Quelle est la différence entre WHERE et HAVING ?
Réponse :

WHERE : Filtre avant le GROUP BY

HAVING : Filtre après le GROUP BY

sql
SELECT Department, AVG(Salary) as AvgSalary
FROM Employees
WHERE Age > 25
GROUP BY Department
HAVING AVG(Salary) > 50000;
23. Qu'est-ce qu'un wildcard en SQL ?
Réponse :
Caractères spéciaux pour la recherche de motifs.

% : Zéro ou plusieurs caractères

_ : Un seul caractère

sql
SELECT * FROM Employees WHERE Name LIKE 'A%';    -- Commence par A
SELECT * FROM Employees WHERE Name LIKE '%son';  -- Finit par son
SELECT * FROM Employees WHERE Name LIKE '_ohn';  -- Un caractère + ohn
24. Qu'est-ce que l'opérateur IN ?
Réponse :
Vérifie si une valeur est dans une liste.

sql
SELECT * FROM Employees WHERE DepartmentID IN (1, 2, 3);

SELECT * FROM Employees 
WHERE DepartmentID IN (SELECT ID FROM Departments WHERE Active = 1);
25. Qu'est-ce que l'opérateur BETWEEN ?
Réponse :
Sélectionne une plage de valeurs.

sql
SELECT * FROM Employees WHERE Age BETWEEN 30 AND 40;
SELECT * FROM Orders WHERE OrderDate BETWEEN '2024-01-01' AND '2024-12-31';
26. Qu'est-ce qu'un CASE statement ?
Réponse :
Logique conditionnelle dans SQL.

sql
SELECT Name,
    CASE
        WHEN Age < 18 THEN 'Minor'
        WHEN Age BETWEEN 18 AND 65 THEN 'Adult'
        ELSE 'Senior'
    END AS AgeGroup
FROM Employees;
27. Qu'est-ce que la fonction COALESCE ?
Réponse :
Retourne la première valeur non-null.

sql
SELECT COALESCE(Phone, Mobile, 'No Phone') AS Contact
FROM Employees;
28. Qu'est-ce qu'une valeur NULL ?
Réponse :
Représente une valeur manquante ou inconnue.

Points importants :

NULL n'est pas égal à NULL

Comparaison avec NULL retourne NULL

Utiliser IS NULL / IS NOT NULL

sql
SELECT * FROM Employees WHERE Phone IS NULL;
SELECT * FROM Employees WHERE COALESCE(Phone, '') != '';
29. Quelle est la différence entre UNION et UNION ALL ?
Réponse :

UNION : Supprime les doublons

UNION ALL : Garde les doublons (plus rapide)

sql
SELECT Name FROM Employees
UNION
SELECT Name FROM Managers;

SELECT Name FROM Employees
UNION ALL
SELECT Name FROM Managers;
30. Quelle est la différence entre CHAR et VARCHAR ?
Réponse :

CHAR : Longueur fixe, complété par des espaces

VARCHAR : Longueur variable

sql
CHAR(10)     -- 'Hello     ' (10 caractères)
VARCHAR(10)  -- 'Hello' (5 caractères stockés)
31. Quelle est la différence entre VARCHAR et NVARCHAR ?
Réponse :

VARCHAR : Caractères non-Unicode

NVARCHAR : Caractères Unicode (support multi-langues)

32. Qu'est-ce qu'un CTE (Common Table Expression) ?
Réponse :
Résultat temporaire nommé, utilisable dans une requête.

sql
WITH EmployeeCTE AS (
    SELECT EmployeeID, Name, DepartmentID
    FROM Employees
    WHERE Salary > 50000
)
SELECT * FROM EmployeeCTE WHERE DepartmentID = 1;
33. Quelle est la différence entre une table temporaire et une variable de table ?
Réponse :

Table temporaire : CREATE TABLE #temp, stockée dans tempdb

Variable de table : DECLARE @table TABLE, en mémoire

34. Qu'est-ce qu'un curseur (Cursor) ?
Réponse :
Parcourt les résultats ligne par ligne.

sql
DECLARE @EmployeeID INT
DECLARE EmployeeCursor CURSOR FOR
SELECT EmployeeID FROM Employees

OPEN EmployeeCursor
FETCH NEXT FROM EmployeeCursor INTO @EmployeeID
WHILE @@FETCH_STATUS = 0
BEGIN
    PRINT @EmployeeID
    FETCH NEXT FROM EmployeeCursor INTO @EmployeeID
END
CLOSE EmployeeCursor
DEALLOCATE EmployeeCursor
35. Quels sont les types de triggers ?
Réponse :

DML Triggers : AFTER, INSTEAD OF

DDL Triggers : CREATE, ALTER, DROP

Logon Triggers : Connexion

36. Qu'est-ce qu'une fonction stockée ?
Réponse :
Fonction SQL qui retourne une valeur.

sql
CREATE FUNCTION GetEmployeeName (@EmployeeID INT)
RETURNS VARCHAR(50)
AS
BEGIN
    DECLARE @Name VARCHAR(50)
    SELECT @Name = Name FROM Employees WHERE EmployeeID = @EmployeeID
    RETURN @Name
END;

SELECT dbo.GetEmployeeName(1);
37. Quelle est la différence entre une fonction scalaire et une fonction table ?
Réponse :

Scalar Function : Retourne une seule valeur

Table-Valued Function : Retourne une table

sql
-- Scalar
CREATE FUNCTION GetFullName(@ID INT)
RETURNS VARCHAR(100)
AS
BEGIN
    RETURN (SELECT FirstName + ' ' + LastName FROM Employees WHERE ID = @ID)
END;

-- Table-Valued
CREATE FUNCTION GetEmployeesByDept(@DeptID INT)
RETURNS TABLE
AS
RETURN (SELECT * FROM Employees WHERE DepartmentID = @DeptID);
38. Qu'est-ce qu'un synonyme ?
Réponse :
Alias pour un objet de base de données.

sql
CREATE SYNONYM Emp FOR HR.dbo.Employees;
SELECT * FROM Emp;
39. Quelle est la différence entre SQL et MySQL ?
Réponse :

SQL : Langage standard de requête

MySQL : Système de gestion de base de données (SGBD) qui utilise SQL

40. Quels sont les SGBD courants ?
Réponse :

MySQL : Open source, populaire

PostgreSQL : Open source, avancé

Oracle : Commercial, entreprise

SQL Server : Microsoft

MongoDB : NoSQL, orienté documents

SQLite : Embarqué, léger

2. Requêtes et Jointures
41. Quels sont les différents types de JOIN ?
Réponse :

INNER JOIN : Lignes correspondantes dans les deux tables

LEFT JOIN : Toutes les lignes de gauche + correspondances

RIGHT JOIN : Toutes les lignes de droite + correspondances

FULL JOIN : Toutes les lignes des deux tables

CROSS JOIN : Produit cartésien

sql
-- INNER JOIN
SELECT e.Name, d.DepartmentName
FROM Employees e
INNER JOIN Departments d ON e.DepartmentID = d.DepartmentID;

-- LEFT JOIN
SELECT e.Name, d.DepartmentName
FROM Employees e
LEFT JOIN Departments d ON e.DepartmentID = d.DepartmentID;
42. Qu'est-ce qu'un self-join ?
Réponse :
Jointure d'une table avec elle-même.

sql
SELECT A.EmployeeID, A.Name AS Employee, B.Name AS Manager
FROM Employees A
INNER JOIN Employees B ON A.ManagerID = B.EmployeeID;
43. Qu'est-ce qu'un CROSS JOIN ?
Réponse :
Produit cartésien de deux tables (toutes les combinaisons).

sql
SELECT A.Name, B.DepartmentName
FROM Employees A
CROSS JOIN Departments B;
-- Si 10 employés et 5 départements = 50 lignes
44. Qu'est-ce qu'un LEFT JOIN ?
Réponse :
Retourne toutes les lignes de la table de gauche et les correspondances de droite.

sql
SELECT e.Name, d.DepartmentName
FROM Employees e
LEFT JOIN Departments d ON e.DepartmentID = d.DepartmentID;
-- Les employés sans département auront NULL pour DepartmentName
45. Qu'est-ce qu'un RIGHT JOIN ?
Réponse :
Retourne toutes les lignes de la table de droite et les correspondances de gauche.

sql
SELECT e.Name, d.DepartmentName
FROM Employees e
RIGHT JOIN Departments d ON e.DepartmentID = d.DepartmentID;
-- Les départements sans employés auront NULL pour Name
46. Qu'est-ce qu'un FULL OUTER JOIN ?
Réponse :
Retourne toutes les lignes des deux tables.

sql
SELECT e.Name, d.DepartmentName
FROM Employees e
FULL OUTER JOIN Departments d ON e.DepartmentID = d.DepartmentID;
47. Comment utiliser EXISTS ?
Réponse :
Vérifie si une sous-requête retourne des lignes.

sql
SELECT * FROM Employees e
WHERE EXISTS (
    SELECT 1 FROM Departments d 
    WHERE d.DepartmentID = e.DepartmentID AND d.Active = 1
);
48. Comment utiliser NOT EXISTS ?
Réponse :
Vérifie qu'une sous-requête ne retourne aucune ligne.

sql
SELECT * FROM Employees e
WHERE NOT EXISTS (
    SELECT 1 FROM Orders o WHERE o.EmployeeID = e.EmployeeID
);
-- Employés sans commandes
49. Quelle est la différence entre IN et EXISTS ?
Réponse :

IN : Compare avec une liste de valeurs

EXISTS : Vérifie l'existence de lignes

Performance :

IN : Mieux pour les petites listes

EXISTS : Mieux pour les grandes tables (s'arrête à la première correspondance)

50. Comment faire une jointure sur plusieurs tables ?
Réponse :

sql
SELECT c.Name, o.OrderDate, p.ProductName, od.Quantity
FROM Customers c
INNER JOIN Orders o ON c.CustomerID = o.CustomerID
INNER JOIN OrderDetails od ON o.OrderID = od.OrderID
INNER JOIN Products p ON od.ProductID = p.ProductID
WHERE o.OrderDate >= '2024-01-01';
51. Comment utiliser GROUP BY ?
Réponse :
Groupe les lignes pour les fonctions d'agrégation.

sql
SELECT DepartmentID, COUNT(*) as EmployeeCount, AVG(Salary) as AvgSalary
FROM Employees
GROUP BY DepartmentID;

SELECT DepartmentID, COUNT(*) as EmployeeCount
FROM Employees
GROUP BY DepartmentID
HAVING COUNT(*) > 5;
52. Quelles sont les fonctions d'agrégation ?
Réponse :

COUNT() : Nombre de lignes

SUM() : Somme

AVG() : Moyenne

MIN() : Minimum

MAX() : Maximum

sql
SELECT 
    COUNT(*) as Total,
    SUM(Salary) as TotalSalary,
    AVG(Salary) as AvgSalary,
    MIN(Salary) as MinSalary,
    MAX(Salary) as MaxSalary
FROM Employees;
53. Comment utiliser DISTINCT ?
Réponse :
Élimine les doublons.

sql
SELECT DISTINCT DepartmentID FROM Employees;
SELECT DISTINCT DepartmentID, JobTitle FROM Employees;
54. Comment utiliser ORDER BY ?
Réponse :
Trie les résultats.

sql
SELECT * FROM Employees ORDER BY Salary DESC;

SELECT * FROM Employees ORDER BY DepartmentID ASC, Salary DESC;

SELECT * FROM Employees ORDER BY COALESCE(Salary, 0) DESC;
55. Comment utiliser LIMIT / TOP ?
Réponse :
Limite le nombre de résultats.

sql
-- MySQL/PostgreSQL
SELECT * FROM Employees LIMIT 10;

-- SQL Server
SELECT TOP 10 * FROM Employees;

-- Oracle
SELECT * FROM Employees WHERE ROWNUM <= 10;

-- Avec OFFSET
SELECT * FROM Employees LIMIT 10 OFFSET 20;
56. Comment utiliser les window functions ?
Réponse :
Calculs sur un ensemble de lignes liées à la ligne courante.

sql
SELECT Name, Salary,
    ROW_NUMBER() OVER (ORDER BY Salary DESC) as RowNum,
    RANK() OVER (ORDER BY Salary DESC) as Rank,
    DENSE_RANK() OVER (ORDER BY Salary DESC) as DenseRank,
    SUM(Salary) OVER (PARTITION BY DepartmentID) as DeptTotal
FROM Employees;
57. Expliquez ROW_NUMBER, RANK, DENSE_RANK, NTILE
Réponse :

ROW_NUMBER : Numéro unique séquentiel

RANK : Rang avec gaps pour ex-aequo

DENSE_RANK : Rang sans gaps

NTILE : Divise en N groupes

text
Données : 100, 100, 90, 80
ROW_NUMBER(): 1, 2, 3, 4
RANK():       1, 1, 3, 4
DENSE_RANK(): 1, 1, 2, 3
NTILE(2):     1, 1, 2, 2
58. Qu'est-ce qu'une requête récursive ?
Réponse :
Requête qui se réfère à elle-même (CTE récursif).

sql
WITH EmployeeHierarchy AS (
    SELECT EmployeeID, ManagerID, Name, 1 as Level
    FROM Employees WHERE ManagerID IS NULL
    UNION ALL
    SELECT e.EmployeeID, e.ManagerID, e.Name, eh.Level + 1
    FROM Employees e
    INNER JOIN EmployeeHierarchy eh ON e.ManagerID = eh.EmployeeID
)
SELECT * FROM EmployeeHierarchy;
59. Quelle est la différence entre CROSS APPLY et OUTER APPLY ?
Réponse :

CROSS APPLY : Comme INNER JOIN, retourne les correspondances

OUTER APPLY : Comme LEFT JOIN, retourne tout de gauche

sql
-- CROSS APPLY
SELECT e.Name, o.OrderDate
FROM Employees e
CROSS APPLY (
    SELECT TOP 3 * FROM Orders WHERE EmployeeID = e.EmployeeID
) o;

-- OUTER APPLY
SELECT e.Name, o.OrderDate
FROM Employees e
OUTER APPLY (
    SELECT TOP 3 * FROM Orders WHERE EmployeeID = e.EmployeeID
) o;
60. Comment implémenter la pagination ?
Réponse :

sql
-- SQL Server
SELECT * FROM Employees
ORDER BY EmployeeID
OFFSET 10 ROWS FETCH NEXT 10 ROWS ONLY;

-- MySQL
SELECT * FROM Employees ORDER BY EmployeeID LIMIT 10 OFFSET 10;

-- Oracle
SELECT * FROM (
    SELECT e.*, ROWNUM rn FROM Employees e
) WHERE rn BETWEEN 11 AND 20;
61. Qu'est-ce que le SQL dynamique ?
Réponse :
SQL généré et exécuté à runtime.

sql
DECLARE @sql NVARCHAR(MAX);
DECLARE @tableName NVARCHAR(50) = 'Employees';
SET @sql = 'SELECT * FROM ' + @tableName;
EXEC sp_executesql @sql;
Risques : Injection SQL, Complexité de debug, Performance overhead

62. Qu'est-ce que FOR XML PATH ?
Réponse :
Convertit les résultats en XML.

sql
SELECT EmployeeID, Name
FROM Employees
FOR XML PATH('Employee');
63. Comment utiliser JSON dans SQL Server ?
Réponse :

sql
-- Générer du JSON
SELECT Name, Age FROM Employees FOR JSON AUTO;

-- Parser du JSON
SELECT * FROM OPENJSON(@json)
WITH (
    Name NVARCHAR(50) '$.name',
    Age INT '$.age'
);

-- Modifier du JSON
SELECT JSON_MODIFY(@json, '$.name', 'NewName');
64. Comment faire un PIVOT ?
Réponse :
Transforme les lignes en colonnes.

sql
SELECT *
FROM (
    SELECT Department, YEAR(HireDate) as Year, Salary
    FROM Employees
) AS SourceTable
PIVOT (
    SUM(Salary)
    FOR Year IN ([2020], [2021], [2022], [2023])
) AS PivotTable;
65. Comment faire un UNPIVOT ?
Réponse :
Transforme les colonnes en lignes.

sql
SELECT Department, Year, Salary
FROM (
    SELECT Department, [2020], [2021], [2022] FROM EmployeePivot
) AS p
UNPIVOT (
    Salary FOR Year IN ([2020], [2021], [2022])
) AS unpvt;
3. Index et Optimisation
66. Qu'est-ce qu'un index clusterisé ?
Réponse :
Index qui détermine l'ordre physique des données.

sql
CREATE CLUSTERED INDEX idx_employee_id ON Employees(EmployeeID);
Caractéristiques : Un seul par table, Les données sont stockées dans cet ordre

67. Qu'est-ce qu'un index non-clusterisé ?
Réponse :
Index séparé des données avec des pointeurs.

sql
CREATE INDEX idx_employee_name ON Employees(Name);
Caractéristiques : Plusieurs par table, Structure séparée

68. Comment optimiser une requête ?
Réponse :

Index : Créer des index appropriés

SELECT : Éviter SELECT *

JOIN : Préférer les JOIN aux sous-requêtes

WHERE : Éviter les fonctions sur les colonnes indexées

LIMIT : Utiliser pour limiter les résultats

EXPLAIN : Analyser le plan d'exécution

69. Qu'est-ce qu'un plan d'exécution ?
Réponse :
Décomposition de la façon dont SQL Server exécute une requête.

sql
EXPLAIN SELECT * FROM Employees WHERE Name = 'John';
-- Ou
SET SHOWPLAN_ALL ON;
70. Comment éviter les fonctions sur les colonnes indexées ?
Réponse :

sql
-- Mauvais (empêche l'utilisation de l'index)
SELECT * FROM Employees WHERE YEAR(HireDate) = 2024;

-- Bon (utilise l'index)
SELECT * FROM Employees 
WHERE HireDate >= '2024-01-01' AND HireDate < '2025-01-01';
71. Qu'est-ce qu'un index couvrant (Covering Index) ?
Réponse :
Index qui contient toutes les colonnes nécessaires à une requête.

sql
CREATE INDEX idx_covering ON Employees(DepartmentID) 
INCLUDE (Name, Salary);

SELECT Name, Salary FROM Employees WHERE DepartmentID = 1;
72. Qu'est-ce qu'un index filtré ?
Réponse :
Index avec une clause WHERE.

sql
CREATE INDEX idx_active_employees ON Employees(Name)
WHERE Status = 'Active';
73. Qu'est-ce qu'un index full-text ?
Réponse :
Index pour la recherche textuelle.

sql
CREATE FULLTEXT CATALOG ftCatalog AS DEFAULT;
CREATE FULLTEXT INDEX ON Articles(Content)
KEY INDEX PK_Articles;

SELECT * FROM Articles
WHERE CONTAINS(Content, 'database AND performance');
74. Qu'est-ce qu'un index composite ?
Réponse :
Index sur plusieurs colonnes.

sql
CREATE INDEX idx_dept_name ON Employees(DepartmentID, Name);
Ordre important :

(DepartmentID, Name) : Utile pour WHERE DepartmentID = ?

(Name, DepartmentID) : Plus utile pour WHERE Name = ?

75. Comment détecter les index manquants ?
Réponse :

sql
SELECT 
    migs.avg_total_user_cost * migs.avg_user_impact * (migs.user_seeks + migs.user_scans) AS improvement_measure,
    mid.statement AS table_name,
    mid.equality_columns,
    mid.inequality_columns,
    mid.included_columns
FROM sys.dm_db_missing_index_group_stats migs
INNER JOIN sys.dm_db_missing_index_groups mig ON migs.group_handle = mig.index_group_handle
INNER JOIN sys.dm_db_missing_index_details mid ON mig.index_handle = mid.index_handle
ORDER BY improvement_measure DESC;
76. Comment détecter les index inutilisés ?
Réponse :

sql
SELECT 
    OBJECT_NAME(i.object_id) AS TableName,
    i.name AS IndexName,
    ISNULL(s.user_seeks, 0) AS user_seeks,
    ISNULL(s.user_scans, 0) AS user_scans,
    ISNULL(s.user_updates, 0) AS user_updates
FROM sys.indexes i
LEFT JOIN sys.dm_db_index_usage_stats s 
    ON i.object_id = s.object_id AND i.index_id = s.index_id
WHERE OBJECTPROPERTY(i.object_id, 'IsUserTable') = 1
ORDER BY user_seeks + user_scans ASC;
77. Qu'est-ce qu'une statistique ?
Réponse :
Informations sur la distribution des données utilisées par l'optimiseur.

sql
UPDATE STATISTICS Employees;
CREATE STATISTICS stat_name ON Employees(Name);
DBCC SHOW_STATISTICS('Employees', 'stat_name');
78. Comment analyser les requêtes lentes ?
Réponse :

sql
SELECT TOP 10 
    qs.total_elapsed_time / qs.execution_count AS avg_elapsed_time,
    qs.execution_count,
    SUBSTRING(st.text, (qs.statement_start_offset/2)+1,
        ((CASE qs.statement_end_offset
            WHEN -1 THEN DATALENGTH(st.text)
            ELSE qs.statement_end_offset
        END - qs.statement_start_offset)/2) + 1) AS query_text
FROM sys.dm_exec_query_stats qs
CROSS APPLY sys.dm_exec_sql_text(qs.sql_handle) st
ORDER BY avg_elapsed_time DESC;
79. Qu'est-ce qu'un deadlock et comment le résoudre ?
Réponse :
Deux transactions s'attendent mutuellement.

Détection :

sql
DBCC TRACEON(1222, -1);
Résolution : Accéder aux ressources dans le même ordre, Utiliser des timeouts, Réduire la durée des transactions

80. Qu'est-ce que le lock escalation ?
Réponse :
SQL Server convertit les locks de ligne en locks de table quand trop de locks sont acquis.

sql
ALTER TABLE Employees SET (LOCK_ESCALATION = DISABLE);
81. Quels sont les niveaux d'isolation ?
Réponse :

READ UNCOMMITTED : Dirty reads possibles

READ COMMITTED : Pas de dirty reads (défaut)

REPEATABLE READ : Pas de non-repeatable reads

SERIALIZABLE : Isolation complète

SNAPSHOT : Versioning des données

sql
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
82. Qu'est-ce que l'injection SQL ?
Réponse :
Insertion de code SQL malveillant dans une requête.

Exemple vulnérable :

sql
SELECT * FROM Users WHERE Name = '" + userInput + "';
-- Si userInput = "' OR '1'='1", retourne tous les utilisateurs
Prévention :

java
PreparedStatement ps = conn.prepareStatement(
    "SELECT * FROM Users WHERE Name = ?");
ps.setString(1, userInput);
83. Comment prévenir l'injection SQL ?
Réponse :

Requêtes paramétrées : PreparedStatement

Procédures stockées : Paramètres typés

Validation : Vérifier les entrées

ORM : Hibernate, JPA

Principe du moindre privilège : Limiter les permissions

84. Qu'est-ce qu'un plan d'exécution en cache ?
Réponse :
SQL Server cache les plans d'exécution pour réutilisation.

sql
SELECT * FROM sys.dm_exec_cached_plans;
DBCC FREEPROCCACHE;
85. Qu'est-ce que la paramétrisation ?
Réponse :
Réutilisation des plans d'exécution avec des paramètres.

sql
EXEC sp_executesql N'SELECT * FROM Employees WHERE ID = @ID', 
    N'@ID INT', @ID = 1;
4. Transactions et Concurrence
86. Qu'est-ce qu'une transaction imbriquée ?
Réponse :
Transaction dans une autre transaction.

sql
BEGIN TRANSACTION;
    BEGIN TRANSACTION;  -- Transaction imbriquée
    COMMIT;
COMMIT;
87. Qu'est-ce qu'un SAVEPOINT ?
Réponse :
Point de sauvegarde dans une transaction.

sql
BEGIN TRANSACTION;
    INSERT INTO Employees VALUES (1, 'John');
    SAVEPOINT sp1;
    INSERT INTO Employees VALUES (2, 'Jane');
    ROLLBACK TO sp1;  -- Annule seulement l'insertion de Jane
COMMIT;
88. Qu'est-ce que le contrôle de concurrence optimiste ?
Réponse :
Vérifie les conflits avant le commit.

sql
UPDATE Employees 
SET Salary = 60000, Version = Version + 1
WHERE EmployeeID = 1 AND Version = 5;
-- Si 0 lignes affectées, conflit détecté
89. Qu'est-ce que le contrôle de concurrence pessimiste ?
Réponse :
Verrouille les ressources avant modification.

sql
BEGIN TRANSACTION;
    SELECT * FROM Employees WITH (UPDLOCK) WHERE EmployeeID = 1;
    UPDATE Employees SET Salary = 60000 WHERE EmployeeID = 1;
COMMIT;
90. Qu'est-ce qu'un lock ?
Réponse :
Mécanisme de synchronisation pour l'accès concurrent.

Types :

Shared (S) : Lecture

Exclusive (X) : Écriture

Update (U) : Mise à jour

sql
SELECT * FROM sys.dm_tran_locks;
91. Qu'est-ce qu'un blocage ?
Réponse :
Une transaction attend qu'une autre libère un lock.

sql
SELECT 
    blocking.session_id AS blocking_session,
    blocked.session_id AS blocked_session,
    blocked.wait_type,
    blocked.wait_time
FROM sys.dm_exec_requests blocked
JOIN sys.dm_exec_requests blocking 
    ON blocked.blocking_session_id = blocking.session_id
WHERE blocked.blocking_session_id > 0;
92. Comment tuer une session bloquante ?
Réponse :

sql
-- Voir les sessions
SELECT * FROM sys.dm_exec_sessions;

-- Tuer une session
KILL 52;  -- 52 = session_id

-- Tuer avec rollback
KILL 52 WITH ROLLBACK IMMEDIATE;
93. Qu'est-ce que le MVCC ?
Réponse :
Multi-Version Concurrency Control : versioning des données pour éviter les locks.

Implémentations :

SQL Server : SNAPSHOT isolation

PostgreSQL : MVCC natif

Oracle : Read consistency

94. Qu'est-ce que le snapshot isolation ?
Réponse :
Lecture des données versionnées sans locks.

sql
ALTER DATABASE MyDB SET ALLOW_SNAPSHOT_ISOLATION ON;

SET TRANSACTION ISOLATION LEVEL SNAPSHOT;
BEGIN TRANSACTION;
    SELECT * FROM Employees;  -- Version au début de la transaction
COMMIT;
95. Qu'est-ce que la cohérence de lecture ?
Réponse :
Garantir que les lectures voient un état cohérent.

Types :

Read Committed : Voit les commits

Repeatable Read : Même résultat dans la transaction

Serializable : Isolation complète

5. Administration
96. Comment migrer une base de données ?
Réponse :

Backup/Restore : Sauvegarde et restauration

Detach/Attach : Détacher et attacher

SSIS : SQL Server Integration Services

Replication : Réplication

Scripts : Génération de scripts

97. Qu'est-ce que la réplication ?
Réponse :
Copie et distribution de données entre bases.

Types :

Snapshot : Copie complète périodique

Transactional : Changements en temps réel

Merge : Synchronisation bidirectionnelle

98. Qu'est-ce que le log shipping ?
Réponse :
Backup automatique des logs de transaction et restauration sur un serveur secondaire.

Composants : Serveur primaire, Serveur secondaire, Serveur de monitoring

99. Qu'est-ce que le database mirroring ?
Réponse :
Duplication de la base sur un serveur standby pour haute disponibilité.

Modes :

High Safety : Synchrone

High Performance : Asynchrone

100. Qu'est-ce que Always On Availability Groups ?
Réponse :
Solution de haute disponibilité et reprise après sinistre.

Avantages : Basculement automatique, Réplicas lisibles, Pas de stockage partagé

6. Questions Pratiques
101. Trouver le Nième salaire maximum
Réponse :

sql
-- Avec sous-requête
SELECT DISTINCT Salary
FROM Employees
ORDER BY Salary DESC
LIMIT 1 OFFSET N-1;

-- Avec DENSE_RANK
SELECT Salary FROM (
    SELECT Salary, DENSE_RANK() OVER (ORDER BY Salary DESC) as rnk
    FROM Employees
) t WHERE rnk = N;

-- Pour le 2ème salaire max
SELECT MAX(Salary) FROM Employees
WHERE Salary < (SELECT MAX(Salary) FROM Employees);
102. Trouver les doublons
Réponse :

sql
SELECT Email, COUNT(*) as Count
FROM Employees
GROUP BY Email
HAVING COUNT(*) > 1;

-- Supprimer les doublons
DELETE FROM Employees
WHERE EmployeeID NOT IN (
    SELECT MIN(EmployeeID) FROM Employees GROUP BY Email
);
103. Trouver les enregistrements sans correspondance
Réponse :

sql
-- LEFT JOIN avec IS NULL
SELECT e.Name FROM Employees e
LEFT JOIN Departments d ON e.DepartmentID = d.DepartmentID
WHERE d.DepartmentID IS NULL;

-- NOT EXISTS
SELECT Name FROM Employees e
WHERE NOT EXISTS (
    SELECT 1 FROM Departments d WHERE d.DepartmentID = e.DepartmentID
);
104. Calculer un total cumulatif
Réponse :

sql
SELECT OrderDate, Amount,
    SUM(Amount) OVER (ORDER BY OrderDate) as RunningTotal
FROM Orders;
105. Trouver les employés avec le même salaire que leur manager
Réponse :

sql
SELECT e.Name as Employee, m.Name as Manager
FROM Employees e
JOIN Employees m ON e.ManagerID = m.EmployeeID
WHERE e.Salary = m.Salary;
106. Trouver le département avec le plus d'employés
Réponse :

sql
SELECT DepartmentID, COUNT(*) as EmployeeCount
FROM Employees
GROUP BY DepartmentID
ORDER BY EmployeeCount DESC
LIMIT 1;
107. Trouver les employés embauchés cette année
Réponse :

sql
SELECT * FROM Employees
WHERE YEAR(HireDate) = YEAR(CURDATE());

-- Ou plus performant
SELECT * FROM Employees
WHERE HireDate >= '2024-01-01' AND HireDate < '2025-01-01';
108. Mettre à jour avec JOIN
Réponse :

sql
UPDATE e
SET e.DepartmentName = d.DepartmentName
FROM Employees e
JOIN Departments d ON e.DepartmentID = d.DepartmentID;
109. Insérer des données depuis une autre table
Réponse :

sql
INSERT INTO EmployeesBackup (EmployeeID, Name, Salary)
SELECT EmployeeID, Name, Salary
FROM Employees
WHERE HireDate < '2020-01-01';
110. Créer une table à partir d'une requête
Réponse :

sql
SELECT * INTO EmployeesBackup
FROM Employees
WHERE DepartmentID = 1;
111. Trouver les ventes par mois
Réponse :

sql
SELECT 
    YEAR(OrderDate) as Year,
    MONTH(OrderDate) as Month,
    SUM(Amount) as TotalSales
FROM Orders
GROUP BY YEAR(OrderDate), MONTH(OrderDate)
ORDER BY Year, Month;
112. Calculer le pourcentage
Réponse :

sql
SELECT 
    DepartmentID,
    COUNT(*) as EmpCount,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM Employees), 2) as Percentage
FROM Employees
GROUP BY DepartmentID;
113. Trouver les N premiers enregistrements par groupe
Réponse :

sql
SELECT * FROM (
    SELECT *,
        ROW_NUMBER() OVER (PARTITION BY DepartmentID ORDER BY Salary DESC) as rn
    FROM Employees
) t WHERE rn <= 3;
114. Concaténer des valeurs
Réponse :

sql
-- SQL Server
SELECT STRING_AGG(Name, ', ') FROM Employees;

-- MySQL
SELECT GROUP_CONCAT(Name SEPARATOR ', ') FROM Employees;
115. Trouver les jours entre deux dates
Réponse :

sql
-- SQL Server
SELECT DATEDIFF(DAY, '2024-01-01', '2024-12-31');  -- 365

-- MySQL
SELECT DATEDIFF('2024-12-31', '2024-01-01');  -- 365
116. Formater une date
Réponse :

sql
-- SQL Server
SELECT FORMAT(GETDATE(), 'yyyy-MM-dd HH:mm:ss');

-- MySQL
SELECT DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s');
117. Gérer les fuseaux horaires
Réponse :

sql
-- SQL Server
SELECT GETUTCDATE();  -- UTC
SELECT SYSDATETIMEOFFSET();  -- Avec offset

-- MySQL
SELECT UTC_TIMESTAMP();  -- UTC
SELECT CONVERT_TZ(NOW(), '+00:00', '+05:30');  -- Conversion
118. Trouver les tables contenant une colonne spécifique
Réponse :

sql
SELECT TABLE_NAME
FROM INFORMATION_SCHEMA.COLUMNS
WHERE COLUMN_NAME = 'Email';
119. Compter les lignes de toutes les tables
Réponse :

sql
SELECT 
    t.name AS TableName,
    p.rows AS RowCount
FROM sys.tables t
JOIN sys.partitions p ON t.object_id = p.object_id
WHERE p.index_id IN (0, 1)
ORDER BY p.rows DESC;
120. Trouver les procédures stockées contenant un texte
Réponse :

sql
SELECT OBJECT_NAME(object_id), definition
FROM sys.sql_modules
WHERE definition LIKE '%Employee%';
7. Bonnes Pratiques
121. Utiliser des index appropriés
Réponse :

Index sur les colonnes de WHERE, JOIN, ORDER BY

Éviter trop d'index (ralentit les écritures)

Index composites pour requêtes multi-colonnes

Surveiller l'utilisation des index

122. Éviter SELECT *
Réponse :

sql
-- Mauvais
SELECT * FROM Employees;

-- Bon
SELECT EmployeeID, Name, Email FROM Employees;
123. Utiliser des paramètres au lieu de concaténation
Réponse :

sql
-- Vulnérable
@sql = 'SELECT * FROM Users WHERE Name = ''\' + @name + ''\'';

-- Sûr
EXEC sp_executesql 
    N'SELECT * FROM Users WHERE Name = @Name',
    N'@Name NVARCHAR(50)',
    @Name = @name;
124. Limiter les résultats
Réponse :

sql
SELECT TOP 100 * FROM LargeTable;
-- Ou
SELECT * FROM LargeTable LIMIT 100;
125. Utiliser EXISTS au lieu de IN
Réponse :

sql
-- Moins performant
SELECT * FROM Employees
WHERE DepartmentID IN (SELECT DepartmentID FROM Departments WHERE Active = 1);

-- Plus performant
SELECT * FROM Employees e
WHERE EXISTS (
    SELECT 1 FROM Departments d 
    WHERE d.DepartmentID = e.DepartmentID AND d.Active = 1
);
126. Utiliser des transactions appropriées
Réponse :

sql
BEGIN TRY
    BEGIN TRANSACTION;
        -- Opérations
    COMMIT;
END TRY
BEGIN CATCH
    ROLLBACK;
    THROW;
END CATCH;
127. Documenter le schéma
Réponse :

sql
EXEC sp_addextendedproperty 
    @name = N'MS_Description',
    @value = N'Table des employés',
    @level0type = N'SCHEMA', @level0name = 'dbo',
    @level1type = N'TABLE', @level1name = 'Employees';

EXEC sp_addextendedproperty 
    @name = N'MS_Description',
    @value = N'Identifiant unique',
    @level0type = N'SCHEMA', @level0name = 'dbo',
    @level1type = N'TABLE', @level1name = 'Employees',
    @level2type = N'COLUMN', @level2name = 'EmployeeID';
128. Sauvegarder régulièrement
Réponse :

sql
-- Backup complet
BACKUP DATABASE MyDB TO DISK = 'C:\\Backup\\MyDB.bak';

-- Backup différentiel
BACKUP DATABASE MyDB TO DISK = 'C:\\Backup\\MyDB_diff.bak' WITH DIFFERENTIAL;

-- Backup du log
BACKUP LOG MyDB TO DISK = 'C:\\Backup\\MyDB_log.trn';
129. Surveiller les performances
Réponse :

sql
SELECT TOP 10 
    qs.total_elapsed_time / qs.execution_count AS avg_elapsed_time,
    qs.execution_count,
    SUBSTRING(st.text, (qs.statement_start_offset/2)+1,
        ((CASE qs.statement_end_offset
            WHEN -1 THEN DATALENGTH(st.text)
            ELSE qs.statement_end_offset
        END - qs.statement_start_offset)/2) + 1) AS query_text
FROM sys.dm_exec_query_stats qs
CROSS APPLY sys.dm_exec_sql_text(qs.sql_handle) st
ORDER BY avg_elapsed_time DESC;
130. Utiliser des vues pour simplifier
Réponse :

sql
CREATE VIEW ActiveEmployees AS
SELECT EmployeeID, Name, Department
FROM Employees
WHERE Status = 'Active';

SELECT * FROM ActiveEmployees WHERE Department = 'IT';
Fin du fichier SQL.md
