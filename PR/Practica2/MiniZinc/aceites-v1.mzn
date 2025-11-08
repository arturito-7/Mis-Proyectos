% - Variables de entrada -----------------------------------------------------------------

int: VALOR; % Precio de venta del producto en €

set of int: aceites = 1..5; % Aceites 1 - VEG1, 2 - VEG2, 3 - ANV1, 4 - ANV2, 5 - ANV3

set of aceites: vegs = 1..2; % Aceites vegetales
set of aceites: anvs = aceites diff vegs; % Aceites NO vegetales

set of int: meses = 1..6; % Meses

array[aceites] of float: dureza; % Dureza de cada tipo de aceite

array[meses, aceites] of int: precios; % Precio de cada tipo de aceite en cada mes en €

int: MAXV; % Máximas toneladas vegetales que se pueden refinar en un mes
int: MAXN; % Máximas toneladas NO vegetales que se pueden refinar en un mes

int: MCAP; % Capacidad máxima de almacenamiento para cada aceite en toneladas

set of int: cap = 0..MCAP; % Capacidad de un almacén en toneladas

int: CA; % Coste de almacenamiento en € por tonelada cada mes

% En caso de que se usen aceites NO vegetales, la dureza debe estar entre MinD y MaxD
float: MinD;
float: MaxD;

int: MinB; % Mínimo beneficio a obtener  en €

array[aceites] of cap: inicial; % Cantidad inicial de cada tipo de aceite en toneladas

int: PV; % Porcentaje de variación máxima de las Existencias iniciales (Ei) del periodo respecto a las Existencias finales (Ef) del periodo

% ----------------------------------------------------------------------------------------

% - Variables de salida ------------------------------------------------------------------

array[meses, aceites] of var cap: compras; % Para cada tipo de aceite en cada mes, las Compras en toneladas

array[meses, aceites] of var cap: almacen; % Para cada tipo de aceite en cada mes, las Ei en toneladas

array[meses, aceites] of var cap: ventas; % Para cada tipo de aceite en cada mes, la cantidad de aceite que se refina y se vende en toneladas

var int: beneficio; % Beneficio total obtenido en €

% ----------------------------------------------------------------------------------------

% - Asserts ------------------------------------------------------------------------------

constraint assert(VALOR > 0, "El precio de venta (VALOR) debe ser positivo");

constraint assert(MAXV >= 0 /\ MAXN >= 0, "Las cantidades máximas de refinado (MAXV, MAXN) deben ser >= 0");

constraint assert(MCAP >= 0, "La capacidad máxima de almacenamiento (MCAP) debe ser >= 0");

constraint assert(CA >= 0, "El coste de almacenamiento por tonelada (CA) debe ser >= 0");

constraint assert(0 <= MinD /\ MinD <= MaxD, "El intervalo de dureza (MinD, MaxD) debe cumplir 0 <= MinD <= MaxD");

constraint assert(PV >= 0, "El porcentaje de variación (PV) debe ser >= 0");

constraint assert(MinB >= 0, "El beneficio mínimo (MinB) debe ser >= 0");

constraint assert(forall(p in precios) (p >= 0), "El precio de cada aceite en cada mes debe ser >= 0");

constraint assert(forall(d in dureza) (d >= 0.0), "La dureza de cada tipo de aceite debe ser >= 0.0");

% ----------------------------------------------------------------------------------------

% - Funciones ----------------------------------------------------------------------------

% Dado un valor entero i y un porcentaje entero p, devuelve p % de i
function int: variacion(int: i, int: p) = i * p div 100;

% Dado un valor entero i y un porcentaje entero p, devuelve el rango de variación de i en p %
function set of int: rango_variacion(int: i, int: p) = i-variacion(i,p)..i+variacion(i,p);

% ----------------------------------------------------------------------------------------

% - Restricciones ------------------------------------------------------------------------

% Nunca se refina mas del máximo permitido para cada tipo de aceite
constraint forall(m in meses) (sum(a in vegs) (ventas[m, a]) <= MAXV);

constraint forall(m in meses) (sum(a in anvs) (ventas[m, a]) <= MAXN);

% Las Ei del primer mes son las iniciales del periodo
constraint forall(a in aceites) (almacen[min(meses), a] == inicial[a]);

% El resto de meses las Ei son Ef del mes anterior, que se obtienen con la siguiente formula: Ei + Compras = Ef + Ventas
constraint forall(m in meses diff {min(meses)}, a in aceites) (almacen[m - 1, a] + compras[m - 1, a] == almacen[m, a] + ventas[m - 1, a]);

% Las Ei más las Compras de un mes no pueden superar el MCAP ya que no cabrían en el almacén
constraint forall(m in meses, a in aceites) (almacen[m, a] + compras[m, a] <= MCAP);

% En caso de que se usen aceites NO vegetales, la dureza final debe estar entre MinD y MaxD
constraint forall(m in meses) (sum(a in anvs) (ventas[m, a]) > 0 ->
    sum(a in aceites) (dureza[a] * ventas[m, a]) >= MinD * sum(a in aceites) (ventas[m, a]) 
    /\
    sum(a in aceites) (dureza[a] * ventas[m, a]) <= MaxD * sum(a in aceites) (ventas[m, a])
);

% Las Ef del último mes no varían más de PV de las Ei del periodo
constraint forall(a in aceites) (almacen[max(meses), a] + compras[max(meses), a] - ventas[max(meses), a] in rango_variacion(inicial[a], PV));

% El beneficio total se obtiene de las ventas - las compras - los costes de almacenamiento
constraint beneficio == sum(m in meses, a in aceites) (ventas[m, a] * VALOR - compras[m, a] * precios[m, a] - almacen[m, a] * CA);

% El beneficio obentido supera el minimo establecido
constraint beneficio >= MinB;

% ----------------------------------------------------------------------------------------

% - Optimización -------------------------------------------------------------------------

% Maximizar el beneficio 
solve maximize beneficio;

% ----------------------------------------------------------------------------------------

output [
  "Compras:\n",
  concat([
    concat([show(compras[m, a]) ++ if m < max(meses) then "\t" else "\n" endif | m in meses]) 
  | a in aceites]),
  
  "\nAlmacén:\n",
  concat([ 
    concat([show(almacen[m, a]) ++ if m < max(meses) then "\t" else "\n" endif | m in meses]) 
  | a in aceites]),
  
  "\nVentas:\n",
  concat([ 
    concat([show(ventas[m, a]) ++ if m < max(meses) then "\t" else "\n" endif | m in meses]) 
  | a in aceites]),
  
  "\nDurezas:\n",
  concat([
    show_float(1, 2, sum(a in aceites) (dureza[a] * ventas[m, a]) / max(sum(a in aceites) (ventas[m, a]), 1)) 
    ++ if m < max(meses) then "\t" else "\n" endif
  | m in meses]),
  
  "\nExistencias finales:\n",
  concat([show( almacen[max(meses), a] + compras[max(meses), a] - ventas[max(meses), a] ) ++ "\n" | a in aceites]),
  
  "\nBeneficio obtenido: \(beneficio) €\n"
];
