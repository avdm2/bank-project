// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: converter.proto

package ru.tinkoff.hse.lib;

public interface ConvertRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:converter.ConvertRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string from_currency = 1;</code>
   * @return The fromCurrency.
   */
  java.lang.String getFromCurrency();
  /**
   * <code>string from_currency = 1;</code>
   * @return The bytes for fromCurrency.
   */
  com.google.protobuf.ByteString
      getFromCurrencyBytes();

  /**
   * <code>string to_currency = 2;</code>
   * @return The toCurrency.
   */
  java.lang.String getToCurrency();
  /**
   * <code>string to_currency = 2;</code>
   * @return The bytes for toCurrency.
   */
  com.google.protobuf.ByteString
      getToCurrencyBytes();

  /**
   * <code>string amount = 3;</code>
   * @return The amount.
   */
  java.lang.String getAmount();
  /**
   * <code>string amount = 3;</code>
   * @return The bytes for amount.
   */
  com.google.protobuf.ByteString
      getAmountBytes();
}
