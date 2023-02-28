package xie.fractal.web.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
// TODO 没有效果？？？
public class AppWebBindingInitializer extends ConfigurableWebBindingInitializer {

	@Override
	public void initBinder(WebDataBinder binder) {
		super.initBinder(binder);

		log.info("initBinder: {}", binder);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		log.info("dateFormat: {}", dateFormat);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		log.info("Date: {}", CustomDateEditor.class);
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		log.info("String: {}", StringTrimmerEditor.class);
	}
}
