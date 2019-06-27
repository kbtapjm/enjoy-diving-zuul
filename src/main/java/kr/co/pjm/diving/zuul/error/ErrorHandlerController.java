package kr.co.pjm.diving.zuul.error;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.zuul.context.RequestContext;

@RestController
public class ErrorHandlerController implements ErrorController {

  private static final String ERROR_MAPPING = "/error";

  @Override
  public String getErrorPath() {
    return ERROR_MAPPING;
  }

  @RequestMapping(value = ERROR_MAPPING)
  public ResponseEntity<String> error() {

    RequestContext ctx = RequestContext.getCurrentContext();
    Object error = ExceptionUtils.getRootCause((Exception) ctx.get("throwable"));

    // zuul.routes.{proxy}.path �� ���ǵ��� ���� ��û�� ��� ���� ó��
    if (error == null) {

      return new ResponseEntity<String>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    if (error instanceof Exception) {

      return new ResponseEntity<String>("SERVICE_UNAVAILABLE", HttpStatus.SERVICE_UNAVAILABLE);
    }

    // ������� ���� ������ ��� ���� ó��
    return new ResponseEntity<String>("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
