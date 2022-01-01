package io.verbana.livethemes.themes

object VuMeters {
  val Standard: scala.xml.Elem =
    <StandardVuMeter>
      <OnlyMinimumToMaximum Value="false" />
      <Maximum Value="#ff0a0a" />
      <AboveZeroDecibel Value="#ffd00a" />
      <ZeroDecibel Value="#c6f864" />
      <Minimum Value="#0af864" />
    </StandardVuMeter>

  val Overload: scala.xml.Elem =
    <OverloadVuMeter>
      <OnlyMinimumToMaximum Value="true" />
      <Maximum Value="#ff0a0a" />
      <AboveZeroDecibel Value="#ffffff" />
      <ZeroDecibel Value="#ffffff" />
      <Minimum Value="#af0a0a" />
    </OverloadVuMeter>

  val Disabled: scala.xml.Elem =
    <DisabledVuMeter>
      <OnlyMinimumToMaximum Value="false" />
      <Maximum Value="#ff0a0a" />
      <AboveZeroDecibel Value="#ffd00a" />
      <ZeroDecibel Value="#828282" />
      <Minimum Value="#6e6e6e" />
    </DisabledVuMeter>

  val Headphones: scala.xml.Elem =
    <HeadphonesVuMeter>
      <OnlyMinimumToMaximum Value="false" />
      <Maximum Value="#a5a5f1" />
      <AboveZeroDecibel Value="#90aaec" />
      <ZeroDecibel Value="#90aaec" />
      <Minimum Value="#0affff" />
    </HeadphonesVuMeter>

  val SendsOnly: scala.xml.Elem =
    <SendsOnlyVuMeter>
      <OnlyMinimumToMaximum Value="false" />
      <Maximum Value="#c8c800" />
      <AboveZeroDecibel Value="#c8c800" />
      <ZeroDecibel Value="#6464ff" />
      <Minimum Value="#6464ff" />
    </SendsOnlyVuMeter>

  val BipolarGainReduction: scala.xml.Elem =
    <BipolarGainReductionVuMeter>
      <OnlyMinimumToMaximum Value="false" />
      <Maximum Value="#5577c6" />
      <AboveZeroDecibel Value="#5577c6" />
      <ZeroDecibel Value="#ffa519" />
      <Minimum Value="#ffa519" />
    </BipolarGainReductionVuMeter>

  val Orange: scala.xml.Elem =
    <OrangeVuMeter>
      <OnlyMinimumToMaximum Value="true" />
      <Maximum Value="#ffa519" />
      <AboveZeroDecibel Value="#ffa519" />
      <ZeroDecibel Value="#ffa519" />
      <Minimum Value="#ffa519" />
    </OrangeVuMeter>

  val All = Seq(
    Standard, Overload, Disabled, Headphones, SendsOnly, BipolarGainReduction, Orange
  )
}
