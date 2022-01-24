import com.simple.sharding.Application;
import com.simple.sharding.entity.UserDO;
import com.simple.sharding.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OrderMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectById() {
        UserDO order = userMapper.selectById(777);
        System.out.println(order);
    }

    @Test
    public void testSelectListByUserId() {
        List<UserDO> orders = userMapper.selectListByName("1");
        System.out.println(orders.size());
    }

    @Test
    public void testSelectOrderList() {
        List<UserDO> orders = userMapper.selectUserList();
        System.out.println(orders);

    }

    @Test
    public void testInsert() {
        UserDO order = new UserDO();
        order.setName("235");
        userMapper.insert(order);
    }
}