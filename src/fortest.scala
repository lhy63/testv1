
import spinal.core._
import spinal.core.sim._
import spinal.lib._
import spinal.sim._


class Sub extends Component{
  val a = in Bits(8 bits)simPublic()
  val b = out(RegNext(a) init 0)simPublic()
}
case class Top() extends Component{
  val myclk,myrst = in Bool()
  val a = in Bits(8 bits)
  val b = in Bits(8 bits)

  val mycd: ClockDomain = ClockDomain(myclk,myrst)
  val u_sub0 = mycd(new Sub)       //mycd时钟包裹Sub模块的例化，则Sub模块使用时钟域mycd
  u_sub0.a := a
}

object forTeat extends App{
  val compiled = SimConfig.withFstWave.allOptimisation.compile(Top())
  compiled.doSim { dut =>
    dut.mycd.forkStimulus(10)
    dut.a #= 0
    dut.mycd.waitSampling()
    dut.a #= 15
    dut.mycd.waitSampling()
    dut.mycd.waitSampling()
    val result = dut.u_sub0.b.toInt
    println(result)
    println("test again")
  }
}