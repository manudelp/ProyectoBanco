package ar.edu.usal.modelo.entidades;

public enum CriptoTipo {
    BTC(Moneda.BTC),
    ETH(Moneda.ETH),
    USDT(Moneda.USDT),
    ADA(Moneda.ADA),
    SOL(Moneda.SOL);

    private final Moneda moneda;

    CriptoTipo(Moneda moneda) {
        this.moneda = moneda;
    }

    public Moneda getMoneda() {
        return moneda;
    }
}
