package com.example.prana_000.stoichiometrycalculator;

public class PeriodicTable {
    final static int NUM_ELEMENTS = 109;
    public Element[] PERIODIC_TABLE = new Element[NUM_ELEMENTS];

    public PeriodicTable() {
        loadTable();
    }

    private void loadTable() {
        String[] elementNames = {"Hydrogen",
                "Helium",
                "Lithium" ,
                "Beryllium",
                "Boron",
                "Carbon",
                "Nitrogen",
                "Oxygen",
                "Fluorine",
                "Neon",
                "Sodium",
                "Magnesium",
                "Aluminum",
                "Silicon",
                "Phosphorus",
                "Sulfur",
                "Chlorine",
                "Argon",
                "Potassium",
                "Calcium",
                "Scandium",
                "Titanium",
                "Vanadium",
                "Chromium",
                "Manganese",
                "Iron",
                "Cobalt",
                "Nickel",
                "Copper",
                "Zinc",
                "Gallium",
                "Germanium",
                "Arsenic",
                "Selenium",
                "Bromine",
                "Krypton",
                "Rubidium",
                "Strontium",
                "Yttrium",
                "Zirconium",
                "Niobium",
                "Molybdenum",
                "Technetium",
                "Ruthenium",
                "Rhodium",
                "Palladium",
                "Silver",
                "Cadmium",
                "Indium",
                "Tin",
                "Antimony",
                "Tellurium",
                "Iodine",
                "Xenon",
                "Cesium",
                "Barium",
                "Lanthanum",
                "Cerium",
                "Praseodymium",
                "Neodymium",
                "Promethium",
                "Samarium",
                "Europium",
                "Gadolinium",
                "Terbium",
                "Dysprosium",
                "Holmium",
                "Erbium",
                "Thulium",
                "Ytterbium",
                "Lutetium",
                "Hafnium",
                "Tantalum",
                "Tungsten",
                "Rhenium",
                "Osmium",
                "Iridium",
                "Platinum",
                "Gold",
                "Mercury",
                "Thallium",
                "Lead",
                "Bismuth",
                "Polonium",
                "Astatine",
                "Radon",
                "Francium",
                "Radium",
                "Actinium",
                "Thorium",
                "Protactinium",
                "Uranium",
                "Neptunium",
                "Plutonium",
                "Americium",
                "Curium",
                "Berkelium",
                "Californium",
                "Einsteinium",
                "Fermium",
                "Mendelevium",
                "Nobelium",
                "Rutherfordium",
                "Dubnium",
                "Seaborgium",
                "Bohrium",
                "Lawrencium",
                "Hassium",
                "Meitnerium"};

        String[] elementSymbols = new String[]{"H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni",
                "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd",
                "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th", "Pa", "U",
                "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt"};

        double[] molarMasses = new double[]{1.0079, 4.0026, 6.941, 9.0122, 10.811, 12.0107, 14.0067, 15.9994, 18.9984, 20.1797, 22.9897, 24.305, 26.9815, 28.0855, 30.9738, 32.065, 35.453, 39.0983,
                39.0983, 40.078, 44.9559, 47.867, 50.9415, 51.9961, 54.938, 55.845, 58.6934, 58.9332, 63.546, 65.39, 69.723, 72.64, 74.9216, 78.96, 79.904, 83.8, 85.4678, 87.62, 88.9059, 91.224, 92.9064,
                95.94, 98.00, 101.07, 102.905, 106.42, 107.868, 112.411, 114.818, 118.71, 121.76, 126.904, 127.6, 131.293, 132.905, 137.327, 138.905, 140.116, 140.907, 144.24, 145, 150.36, 151.964, 157.25,
                158.925, 162.5, 164.930, 167.259, 168.934, 173.04, 174.967, 178.49, 180.947, 183.84, 186.207, 190.23, 192.217, 195.078, 196.966, 200.59, 204.383, 207.2, 208.980, 208.982, 209, 210, 222, 223,
                226, 227, 231.035, 232.038, 237, 238.028, 243, 244, 247, 247, 251, 252, 257, 258, 259, 261, 262, 262, 264, 266, 268};

        for (int i = 0; i < NUM_ELEMENTS; i++) {
            PERIODIC_TABLE[i] = new Element(elementNames[i], molarMasses[i], elementSymbols[i], i + 1);
        }


    }
    /**
     * @param atomicNumber Must be 1 or more
     * @return The element with the specified atomic number.
     */
    public Element getElement(int atomicNumber) {
        return PERIODIC_TABLE[atomicNumber - 1];
    }


    /**
     *
     * @param symbol The symbol of the element
     * @return The element with the specified symbol
     */
    public Element getElement(String symbol) {
        for (Element e : PERIODIC_TABLE) {
            if (e.getSymbol().equals(symbol)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String rtn = "";
        for (Element e : PERIODIC_TABLE) {
            rtn += e + "\n";
        }

        return rtn;
    }
}
