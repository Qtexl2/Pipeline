package by.local.test.dto;

import by.local.test.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionDTO {
   private Type type;

    public void setType(String type) {
        try{
            this.type = Type.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e){
            throw new ApiException(ApiException.Message.INCORRECT_ACTION.getText(), HttpStatus.BAD_REQUEST);
        }
    }

    public enum Type {
       PRINT,
       RANDOM,
       COMPLITED,
       DELAYED
   }
}
