package ch.frankel.blog.sessions;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class IndexController {

    private final Counter counter;

    public IndexController(Counter counter) {
        this.counter = counter;
    }

    @ModelAttribute("hostname")
    private String hostname() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/")
    public String index(Model model) {
        counter.incrementValue();
        model.addAttribute("counter", counter.getValue());
        return "index";
    }
}
