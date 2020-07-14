package cn.kj120.study;


@PCompoment(value = "test")
@PController
public class DemoController {

    @PAutowired
    private TestService testService;

    public DemoController() {
    }

    @PRequestMapping("/demo")
    public String demo(@PRequestParam("name") String name) {
        System.err.println("这是name = " + name);
        return name;
    }

    @PRequestMapping("*")
    public String demo() {
        return testService.helloWorld();
    }
}
