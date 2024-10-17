package Models;

import Records.ConversorDTO;

public class Conversor {
    private String base_code;
    private String target_code;
    private Double conversion_result;
    private Double value_conversion;

    public Conversor(ConversorDTO conversorDto, Double value) {
        this.base_code = conversorDto.base_code();
        this.target_code = conversorDto.target_code();
        this.conversion_result = Double.valueOf( conversorDto.conversion_result());
        this.value_conversion = value;
    }

    @Override
    public String toString() {
        return "El valor " + value_conversion + "[" + base_code + "]" +
                " corresponde al valor final de =>> " + conversion_result + " ["
                + target_code + "].";

    }
}
